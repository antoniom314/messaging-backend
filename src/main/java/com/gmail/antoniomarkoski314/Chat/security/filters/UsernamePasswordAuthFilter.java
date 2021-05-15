package com.gmail.antoniomarkoski314.Chat.security.filters;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.gmail.antoniomarkoski314.Chat.Properties;
import com.gmail.antoniomarkoski314.Chat.controllers.Credentials;
import com.gmail.antoniomarkoski314.Chat.models.User;
import com.gmail.antoniomarkoski314.Chat.security.UserDetailsServiceImpl;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Date;

public class UsernamePasswordAuthFilter extends UsernamePasswordAuthenticationFilter {
    private AuthenticationManager authenticationManager;
    private UserDetailsServiceImpl userDetailsServiceImpl;

    public UsernamePasswordAuthFilter(AuthenticationManager authenticationManager,
                                      UserDetailsServiceImpl userDetailsServiceImpl
    ) {
        this.authenticationManager = authenticationManager;
        this.userDetailsServiceImpl = userDetailsServiceImpl;

        this.setFilterProcessesUrl("/api/login");
        this.setPostOnly(false);
        System.out.println("UsernamePasswordAuthFilter");
    }

    /* Trigger when we issue POST request to /login
    We also need to pass in {"username":"dan", "password":"dan123"} in the request body
     */
    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {

        String header = request.getHeader(Properties.HEADER_STRING);

        if (header != null && header.startsWith(Properties.BASIC_PREFIX)) {
            System.out.println("'Authorization' header= " + header);
            return authenticateUser(header);
        }
        return null;
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response,
                                            FilterChain chain, Authentication authResult) throws IOException, ServletException {
        // Grab userDetails
        UserDetails userDetails = (UserDetails) authResult.getPrincipal();

        System.out.println("successfulAuth() userDetails = " + userDetails.toString());

        // Create JWT Token
        String token = JWT.create()
                .withSubject(userDetails.getUsername())
                .withExpiresAt(new Date(System.currentTimeMillis() + Properties.EXPIRATION_TIME))
                .sign(Algorithm.HMAC512(Properties.SECRET.getBytes()));

        // Add token in response
        response.addHeader(Properties.HEADER_STRING, Properties.TOKEN_PREFIX +  token);

        System.out.println("successfulAuth() response= " + response.getHeader(Properties.HEADER_STRING));
    }

    private Authentication authenticateUser(String header) {

        Credentials credentials = getBasicCredentials(header);

        System.out.println("AUTH credentials" + credentials);
        User user = new User(credentials.getUsername(), credentials.getPassword(),"ROLE_USER","");
        System.out.println("AUTH user.getPassword())= " + user.getPassword());
        UserDetails userDetails = this.userDetailsServiceImpl.loadUserByUsername(user.getUsername());
        System.out.println("AUTH userDetails.getPassword()= " + userDetails.getPassword());

        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                credentials.getUsername(), credentials.getPassword(),
                new ArrayList<>());
        authenticationToken.setDetails(
                userDetails);

        System.out.println("AUTH token= " + authenticationToken);

        try {
            Authentication authentication = authenticationManager.authenticate(authenticationToken);
            System.out.println(authentication);
            SecurityContextHolder.getContext().setAuthentication(authentication);

            return authentication;
        } catch (AuthenticationException exception) {
            System.out.println("AUTH exception= " + exception.getMessage());
        }
        return null;
    }

    private Credentials getBasicCredentials(String header) {

        if (header != null && header.startsWith(Properties.BASIC_PREFIX)) {

            String headerValue = header.substring(Properties.BASIC_PREFIX.length());
            String decodedValue = new String(Base64.getDecoder().decode(headerValue));
            System.out.println(decodedValue);

            String[] substringArray = decodedValue.split(":");
            if (substringArray.length > 1) {

                return new Credentials(substringArray[0], substringArray[1]);
            }
        } else {
            System.out.println("Authentication header is null or it's not Basic");
            return null;
        }
        return null;
    }
}
