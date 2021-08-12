package com.gmail.antoniomarkoski314.Chat.filters;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.gmail.antoniomarkoski314.Chat.Properties;
import com.gmail.antoniomarkoski314.Chat.services.userdetails.UserDetailsServiceImpl;
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

// Authorization filter
public class BasicAuthFilter extends BasicAuthenticationFilter {

    private UserDetailsServiceImpl userDetailsServiceImpl;

    public BasicAuthFilter(AuthenticationManager authenticationManager,
                           UserDetailsServiceImpl userDetailsServiceImpl) {
        super(authenticationManager);
        this.userDetailsServiceImpl = userDetailsServiceImpl;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {

        // Get "Authorization" header
        String header = request.getHeader(Properties.AUTHORIZATION_HEADER);

        // If header does not contain "Bearer" or is null
        if (header == null || !header.startsWith(Properties.TOKEN_PREFIX)) {

            // Continue filter chain
            chain.doFilter(request, response);
            return;
        }

        String token = header.replace(Properties.TOKEN_PREFIX,"");

        Authentication authentication = getUserAuthentication(token);
        // Authorize the user
        SecurityContextHolder.getContext().setAuthentication(authentication);

        // Continue the filter chain
        chain.doFilter(request, response);
    }

    private Authentication getUserAuthentication(String token) {

        if (token != null) {

            // Verify token
            DecodedJWT decoded = JWT.require(Algorithm.HMAC512(Properties.SECRET.getBytes()))
                    .build()
                    .verify(token);
            // Get username from decoded token
            String userName = decoded.getSubject();

            if (userName != null) {
                // Find the registered user by username
                UserDetails userDetails = this.userDetailsServiceImpl.loadUserByUsername(userName);

                // return Authentication with authorities from UserDetails
                return new UsernamePasswordAuthenticationToken(
                        userName, null, userDetails.getAuthorities());
            }
        }
        return null;
    }
}
