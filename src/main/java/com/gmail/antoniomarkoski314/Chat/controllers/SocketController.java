package com.gmail.antoniomarkoski314.Chat.controllers;

import com.gmail.antoniomarkoski314.Chat.models.SocketMessage;
import org.springframework.messaging.handler.annotation.*;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessageType;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin
public class SocketController {

    private static final String BROKER_URL = "/subscribe";

    public SimpMessagingTemplate template;

    public SocketController(SimpMessagingTemplate template) {
        this.template = template;
    }

    // Receive messages from frontend and resend them to selected user
    @MessageMapping("/send")
    public void onReceivedMessage(SocketMessage message) {

        SimpMessageHeaderAccessor headerAccessor =
                SimpMessageHeaderAccessor.create(SimpMessageType.MESSAGE);
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication != null) {
            // Get username from Authentication and add it to the message
            message.setUserFrom(authentication.getName());
            // Send message to selected user
            template.convertAndSendToUser(message.getUserTo(),
                    BROKER_URL, message, headerAccessor.getMessageHeaders());
        }
    }
}
