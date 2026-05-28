package com.easyprofile.demo.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.easyprofile.authlib.dto.response.ApiResponse;
import com.easyprofile.authlib.service.CurrentUserProvider;
import com.easyprofile.demo.dto.FriendshipView;
import com.easyprofile.demo.service.FriendshipService;

@RestController
@RequestMapping("/friendship")
public class FriendshipController {
    
    private final FriendshipService friendshipService;
    private final CurrentUserProvider currentUserProvider;

    public FriendshipController( CurrentUserProvider currentUserProvider, FriendshipService friendshipService ) {

        this.friendshipService = friendshipService;
        this.currentUserProvider = currentUserProvider;

    }

    @PostMapping( "/request/{receiverId}" )
    public ResponseEntity<ApiResponse<FriendshipView>> request( @PathVariable Long receiverId ) {

        Long userId = currentUserProvider.getCurrentUserId();

        FriendshipView view = friendshipService.sendFriendRequest(userId, receiverId);

        return ResponseEntity.ok( ApiResponse.success( "request sent", view ) );

    }

    @PatchMapping( "/accept/{senderId}" )
    public ResponseEntity<ApiResponse<FriendshipView>> accept( @PathVariable Long senderId ) {

        Long userId = currentUserProvider.getCurrentUserId();
        FriendshipView view = friendshipService.confirmFriendRequest(senderId, userId);

        return ResponseEntity.ok( ApiResponse.success( "request accepted", view ) );

    }

    @GetMapping( "/get/requests" )
    public ResponseEntity<ApiResponse<List<FriendshipView>>> getRequests() {

        Long userId = currentUserProvider.getCurrentUserId();

        return ResponseEntity.ok( ApiResponse.success( friendshipService.getNotHandledRequests(userId) ) );

    }

    @GetMapping( "/get/{visitingId}")
    public ResponseEntity<ApiResponse<FriendshipView>> getFriendship( @PathVariable Long visitingId ) {

        Long userId = currentUserProvider.getCurrentUserId();

        return ResponseEntity.ok( ApiResponse.success( friendshipService.getFriendship(visitingId, userId) ) );

    }

    @GetMapping( "/get/accepted" )
    public ResponseEntity<ApiResponse<List<FriendshipView>>> getAccepted() {

        Long userId = currentUserProvider.getCurrentUserId();

        return ResponseEntity.ok( ApiResponse.success( friendshipService.getFriends(userId) ) );

    }

    @GetMapping( "/getship/{friendshipId}" )
    public ResponseEntity<ApiResponse<FriendshipView>> getFriendshipById( @PathVariable Long friendshipId ) {

        return ResponseEntity.ok( ApiResponse.success( friendshipService.findById(friendshipId) ) );

    }

    @DeleteMapping( "/delete/{friendshipId}" )
    public ResponseEntity<ApiResponse<List<FriendshipView>>> deleteFriendshipById( @PathVariable Long friendshipId ) {

        return ResponseEntity.ok( ApiResponse.success( friendshipService.deleteById(friendshipId) ) );

    }

}
