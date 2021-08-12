package com.gmail.antoniomarkoski314.Chat.controllers;

import com.gmail.antoniomarkoski314.Chat.models.SocketMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Headers;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.messaging.simp.SimpMessageType;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.annotation.SendToUser;
import org.springframework.messaging.simp.annotation.SubscribeMapping;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin
public class WebSocketController {

    private String message = "";

    private static final String SENDING_URL = "/queue/subscribe";

    @Autowired
    public SimpMessagingTemplate template;
//    public SimpMessageSendingOperations template;
//    public WebSocketController(SimpMessagingTemplate template) {
//        this.template = template;
//    }

//    @MessageMapping(RECEIVING_URL)
//    public void sendSpecific(
//             String message,
//            @Payload Message msg,
//            Principal user,
//            @Header("simpSessionId") String sessionId) throws Exception {
//        System.out.println("sendSpecific : " + message + " user: " + user + " msg: " + msg);
////        template.convertAndSendToUser(
////                msg.getTo(), "/topic", out);
//    }

    @MessageMapping("/send")
    public void onReceivedMessage(SocketMessage message, @Headers SimpMessageHeaderAccessor header) {
        //public void onReceivedMessage(SocketMessage message, @Header("simpSessionId") String sessionId) {
        SimpMessageHeaderAccessor headerAccessor =
                SimpMessageHeaderAccessor.create(SimpMessageType.MESSAGE);

        System.out.println(header.toString());
        System.out.println(header.getUser());
        System.out.println(header.getSubscriptionId());
        System.out.println(header.getSessionId());

//        headerAccessor.setSessionId(sessionId);
//        headerAccessor.setLeaveMutable(true);

//        template.convertAndSend("/user/queue/subscribe", "message");
        template.convertAndSendToUser("admin",
                "/queue/subscribe", message, headerAccessor.getMessageHeaders());

        System.out.println(message.toString());
        System.out.println("New message received : " + message.getUser() + " - " + message.getMessage());
    }

//    @SubscribeMapping("/subscribe")
//    public String onSubscribe() {
//        System.out.println("SUBSCRIBED " + message);
//        return "SUBSCRIBED : " + message;
//    }

    //@Scheduled(fixedRate = 10000)
    public void sendTestMessage() {
        template.convertAndSend(SENDING_URL, buildMessage("Test message from server "));
    }

    public void sendMessageToUser(String user, String message) {
        template.convertAndSendToUser(user, SENDING_URL, buildMessage(message));
    }

    public void sendMessage(String message) {
        template.convertAndSend(SENDING_URL, buildMessage(message));
    }

    private String buildMessage(String message) {
        System.out.println("Send message " + message);
        return message;
    }
}
