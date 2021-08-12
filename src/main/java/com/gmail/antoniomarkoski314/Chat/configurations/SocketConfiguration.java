package com.gmail.antoniomarkoski314.Chat.configurations;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.gmail.antoniomarkoski314.Chat.Properties;
import com.gmail.antoniomarkoski314.Chat.services.userdetails.UserDetailsServiceImpl;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.config.ChannelRegistration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.messaging.support.MessageHeaderAccessor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.web.socket.AbstractSecurityWebSocketMessageBrokerConfigurer;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;

@Configuration
@EnableWebSocketMessageBroker
@Order(Ordered.HIGHEST_PRECEDENCE)
public class SocketConfiguration extends AbstractSecurityWebSocketMessageBrokerConfigurer {

    private UserDetailsServiceImpl userDetailsServiceImpl;

    public SocketConfiguration(UserDetailsServiceImpl userDetailsServiceImpl) {
        this.userDetailsServiceImpl = userDetailsServiceImpl;
    }

    @Override
    public void configureMessageBroker(MessageBrokerRegistry config) {
        // Set application prefix to messages published by client with "/app/send" destination
        config.setApplicationDestinationPrefixes("/app");
        // Set user destination prefix used by broker to send messages to specific user
        config.setUserDestinationPrefix("/user");
        // Enable broker with "/user" destination prefix to deliver messages to client
        // subscribed with "/user" followed by username followed by "/subscribe"
        config.enableSimpleBroker("/user");
    }

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        // Add endpoint to websockets url to connect with clients using "ws://localhost:8080/socket" url
        registry.addEndpoint("/socket").setAllowedOrigins("*");
    }

    @Override
    protected void customizeClientInboundChannel(ChannelRegistration registration) {
        registration.interceptors(new ChannelInterceptor() {
            @Override
            public Message<?> preSend(Message<?> message, MessageChannel channel) {

                StompHeaderAccessor accessor = MessageHeaderAccessor.getAccessor(
                        message, StompHeaderAccessor.class);
                if (StompCommand.CONNECT.equals(accessor.getCommand())) {

                    String token = accessor.getNativeHeader(Properties.AUTHORIZATION_HEADER).get(0);
                    Authentication authentication = getUserAuthentication(token);
                    accessor.setUser(authentication);
                }
                return message;
            }
        });
    }

    private Authentication getUserAuthentication(String bearerToken) {
        String token = bearerToken.replace(Properties.TOKEN_PREFIX, "");

        if (token != null) {
            System.out.println("SocketConfig getUserAuth token = " + token.toString());
            // Validate token
            DecodedJWT decoded = JWT.require(Algorithm.HMAC512(Properties.SECRET.getBytes()))
                    .build()
                    .verify(token);
            // Get username from decoded token
            String userName = decoded.getSubject();

            // Search in the DB to find the user by username
            if (userName != null) {
                UserDetails userDetails = this.userDetailsServiceImpl.loadUserByUsername(userName);
                System.out.println(userDetails.getUsername());
                System.out.println(userDetails.getAuthorities());
                UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(
                        userName, null, userDetails.getAuthorities());
                return auth;
            }
            return null;
        }
        return null;
    }

    @Override
    protected boolean sameOriginDisabled() {
        return true;
    }
}
