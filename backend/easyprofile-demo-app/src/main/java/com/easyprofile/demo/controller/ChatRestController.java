package com.easyprofile.demo.controller;

import java.security.Principal;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.easyprofile.authlib.dto.response.ApiResponse;
import com.easyprofile.demo.dto.ChatMessageView;
import com.easyprofile.demo.service.ChatService;

@RestController
@RequestMapping( "/chats" )
public class ChatRestController {
    
    private final ChatService chatService;

    public ChatRestController( ChatService chatService ) { this.chatService = chatService; }

    @GetMapping( "/{friendshipId}/messages" )
    public ResponseEntity<ApiResponse<List<ChatMessageView>>> getMessages( @PathVariable Long friendshipId, Principal principal ) {

        return ResponseEntity.ok( ApiResponse.success( chatService.getMessages(friendshipId) ) );

    }

}
