package com.easyprofile.demo.entity;

import java.time.LocalDateTime;

import org.hibernate.annotations.CreationTimestamp;

import com.easyprofile.authlib.entity.UserEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table( name = "messages" )
public class MessageEntity {
    
    @Id
    @GeneratedValue( strategy = GenerationType.IDENTITY )
    private Long id;

    @ManyToOne( fetch = FetchType.LAZY, optional = false )
    @JoinColumn( name = "friendship_id", nullable = false, updatable = false )
    private FriendshipEntity friendship;

    @ManyToOne( fetch = FetchType.LAZY, optional = false )
    @JoinColumn( name = "sender_id", nullable = false, updatable = false )
    private UserEntity sender;

    @Column( updatable = true )
    private String content;

    @Column( nullable = false )
    private boolean isRead = false;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    public MessageEntity() {}

    public MessageEntity( FriendshipEntity friendship, UserEntity sender, String content ) {

        this.friendship = friendship;
        this.sender = sender;
        this.content = content;

    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public FriendshipEntity getFriendship() {
        return friendship;
    }

    public void setFriendship(FriendshipEntity friendship) {
        this.friendship = friendship;
    }

    public UserEntity getSender() {
        return sender;
    }

    public void setSender(UserEntity sender) {
        this.sender = sender;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public boolean isRead() {
        return isRead;
    }

    public void setRead(boolean isRead) {
        this.isRead = isRead;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }


}
