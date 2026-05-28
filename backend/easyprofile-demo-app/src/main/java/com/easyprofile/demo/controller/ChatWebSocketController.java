package com.easyprofile.demo.controller;

import java.security.Principal;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import com.easyprofile.authlib.exception.UnauthorizedException;
import com.easyprofile.demo.dto.ChatMessageRequest;
import com.easyprofile.demo.dto.ChatMessageView;
import com.easyprofile.demo.service.ChatService;

/*

All users connect to the same WebSocket endpoint.

Each user subscribes to one or more destinations(dynamic, they just match strings).

Sending to /app/... means:
"Backend, please process this."

Sending to /topic/... means:
"Broker, deliver this to subscribers." (impractical, just saying)

@MessageMapping handles incoming /app/... messages.

SimpMessagingTemplate sends backend-generated messages to /topic/... destinations.

The simple broker distributes those messages to matching subscribers.

The sender also receives the broadcast if they are subscribed to that destination.

*/

@Controller
public class ChatWebSocketController {
    
    private final ChatService chatService;
    private final SimpMessagingTemplate messagingTemplate;

    public ChatWebSocketController( ChatService chatService, SimpMessagingTemplate messagingTemplate ) {

        this.chatService = chatService;
        this.messagingTemplate = messagingTemplate;

    }

    @MessageMapping("/chat.send")
    public void sendMessage( ChatMessageRequest request, Principal principal ) {
        if (principal == null || principal.getName() == null || principal.getName().isBlank()) {
            throw new UnauthorizedException("WebSocket session is not authenticated");
        }

        ChatMessageView savedMessage = chatService.saveMessage( request, principal.getName() );

        messagingTemplate.convertAndSend( "/topic/chat/" + request.getFriendshipId(), savedMessage );

    }

}
