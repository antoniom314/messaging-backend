package com.gmail.antoniomarkoski314.Chat.security.filters;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.gmail.antoniomarkoski314.Chat.Properties;
import com.gmail.antoniomarkoski314.Chat.security.UserDetailsServiceImpl;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class BasicAuthFilter extends BasicAuthenticationFilter {

    private UserDetailsServiceImpl userDetailsServiceImpl;

    public BasicAuthFilter(AuthenticationManager authenticationManager,
                           UserDetailsServiceImpl userDetailsServiceImpl) {
        super(authenticationManager);
        this.userDetailsServiceImpl = userDetailsServiceImpl;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {

        // Read the Authorization header, where the JWT token should be
        String header = request.getHeader(Properties.HEADER_STRING);

        System.out.println("BAF doFilterInternal response auth header= " + header);

        if (header == null){
            chain.doFilter(request, response);
            return;
        }

        // If header does not contain BEARER or is null delegate to Spring impl and exit
        if (!header.startsWith(Properties.TOKEN_PREFIX)) {
            chain.doFilter(request, response);
            return;
        }

        // If header is present, try grab user details from database and perform authorization
        Authentication authentication = getUsernamePasswordAuthentication(request);
        SecurityContextHolder.getContext().setAuthentication(authentication);

        System.out.println("BAF doFilterInternal authentication= " + authentication);

        // Continue filter execution
        chain.doFilter(request, response);
    }

    private Authentication getUsernamePasswordAuthentication(HttpServletRequest request) {
        String token = request.getHeader(Properties.HEADER_STRING)
                .replace(Properties.TOKEN_PREFIX,"");

        if (token != null) {

            System.out.println("BAF getUsernameAnd.. token = " + token.toString());

            // parse the token and validate it
            String userName = JWT.require(Algorithm.HMAC512(Properties.SECRET.getBytes()))
                    .build()
                    .verify(token)
                    .getSubject();

            // Search in the DB if we find the user by token subject (username)
            // If so, then grab user details and create spring auth token using username, pass, authorities/roles
            if (userName != null) {

                UserDetails userDetails = this.userDetailsServiceImpl.loadUserByUsername(userName);
                //System.out.println("BAF userDetails= " + userDetails.toString());
                UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(
                        userName, null, userDetails.getAuthorities());
                return auth;
            }
            return null;
        }
        return null;
    }
}
