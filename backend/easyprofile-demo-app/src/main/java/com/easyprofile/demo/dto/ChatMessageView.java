package com.easyprofile.demo.dto;

import java.time.LocalDateTime;

public class ChatMessageView {
    
    private Long id;
    private Long friendshipId;
    private Long senderId;
    private String senderName;
    private String content;
    private LocalDateTime createdAt;

    public ChatMessageView( Long id, Long friendshipId, Long senderId, String senderName, String content, LocalDateTime createdAt ) {

        this.id = id;
        this.friendshipId = friendshipId;
        this.senderId = senderId;
        this.senderName = senderName;
        this.content = content;
        this.createdAt = createdAt;

    }

    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public Long getFriendshipId() {
        return friendshipId;
    }
    public void setFriendshipId(Long friendshipId) {
        this.friendshipId = friendshipId;
    }
    public Long getSenderId() {
        return senderId;
    }
    public void setSenderId(Long senderId) {
        this.senderId = senderId;
    }
    public String getSenderName() {
        return senderName;
    }
    public void setSenderName(String senderName) {
        this.senderName = senderName;
    }
    public String getContent() {
        return content;
    }
    public void setContent(String content) {
        this.content = content;
    }
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    

}
