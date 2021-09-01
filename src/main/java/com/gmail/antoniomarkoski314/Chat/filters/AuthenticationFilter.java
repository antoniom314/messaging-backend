package com.gmail.antoniomarkoski314.Chat.filters;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.gmail.antoniomarkoski314.Chat.Properties;
import com.gmail.antoniomarkoski314.Chat.models.Credentials;
import com.gmail.antoniomarkoski314.Chat.services.userdetails.UserDetailsServiceImpl;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.bind.annotation.RequestHeader;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Base64;
import java.util.Date;
import java.util.Enumeration;

public class AuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private AuthenticationManager authenticationManager;
    private UserDetailsServiceImpl userDetailsServiceImpl;

    public AuthenticationFilter(AuthenticationManager authenticationManager,
                                UserDetailsServiceImpl userDetailsServiceImpl
    ) {
        this.authenticationManager = authenticationManager;
        this.userDetailsServiceImpl = userDetailsServiceImpl;

        this.setFilterProcessesUrl(Properties.authenticateUrl);
        this.setPostOnly(false);
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
//        Enumeration<String> headers = request.getHeaderNames();
//        while (headers.hasMoreElements()) {
//            System.out.println(headers.nextElement());
//        }

        String header = request.getHeader(Properties.AUTHENTICATION_HEADER);

        if (header != null && header.startsWith(Properties.BASIC_PREFIX)) {

            Credentials credentials = getBasicCredentials(header);
            Authentication authentication = getAuthentication(credentials);
            return authentication;
        }
        return null;
    }

    private Authentication getAuthentication(Credentials credentials) {

        if (credentials == null) return null;

        // Find registered user
        UserDetails userDetails = this.userDetailsServiceImpl.loadUserByUsername(credentials.getUsername());

        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(credentials.getUsername(), credentials.getPassword());
        authenticationToken.setDetails(userDetails);

        try {
            // Get Authentication
            Authentication authentication = authenticationManager.authenticate(authenticationToken);

            return authentication;
        } catch (AuthenticationException exception) {
            System.out.println("AUTH exception= " + exception.getMessage());
        }
        return null;
    }

    // Get username and password from "Authentication: Basic" header
    private Credentials getBasicCredentials(String header) {

        if (header != null && header.startsWith(Properties.BASIC_PREFIX)) {
            String headerValue = header.substring(Properties.BASIC_PREFIX.length());
            // Decode credentials with Base64 decoder
            String decodedValue = new String(Base64.getDecoder().decode(headerValue));

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

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response,
                                            FilterChain chain, Authentication authResult) throws IOException, ServletException {
        // Get userDetails from Authentication returned by the attemptAuthentication() method
        UserDetails userDetails = (UserDetails) authResult.getPrincipal();

        // Create JWT Token with UserDetails
        String token = JWT.create()
                .withSubject(userDetails.getUsername())
                .withExpiresAt(new Date(System.currentTimeMillis() + Properties.EXPIRATION_TIME))
                .sign(Algorithm.HMAC512(Properties.SECRET.getBytes()));

        // Add token in response
        response.addHeader(Properties.AUTHORIZATION_HEADER, Properties.TOKEN_PREFIX +  token);
    }
}
