package com.easyprofile.demo.dto;

import java.time.LocalDateTime;

import com.easyprofile.demo.enums.FriendshipStatus;

public class FriendshipView {
    
    private Long id;
    private Long senderId;
    private String senderUsername;
    private Long receiverId;
    private String receiverUsername;
    private FriendshipStatus status;
    private LocalDateTime friendSince;

    public FriendshipView(
        Long id,
        Long senderId,
        String senderUsername,
        Long receiverId,
        String receiverUsername,
        FriendshipStatus status,
        LocalDateTime friendSince
    ) {

        this.id = id;
        this.senderId = senderId;
        this.senderUsername = senderUsername;
        this.receiverId = receiverId;
        this.receiverUsername = receiverUsername;
        this.friendSince = friendSince;
        this.status = status;

    }

    public Long getId() {
        return id;
    }

    public Long getSenderId() {
        return senderId;
    }

    public String getSenderUsername() {
        return senderUsername;
    }

    public Long getReceiverId() {
        return receiverId;
    }

    public String getReceiverUsername() {
        return receiverUsername;
    }

    public FriendshipStatus getStatus() {
        return status;
    }

    public LocalDateTime getFriendSince() {
        return friendSince;
    }
}
