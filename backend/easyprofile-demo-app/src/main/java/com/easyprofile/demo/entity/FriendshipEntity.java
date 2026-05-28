package com.easyprofile.demo.entity;

import java.time.LocalDateTime;

import com.easyprofile.authlib.entity.UserEntity;
import com.easyprofile.demo.enums.FriendshipStatus;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table( name = "friendships" )
public class FriendshipEntity {
    
    @Id
    @GeneratedValue( strategy = GenerationType.IDENTITY )
    private Long id;

    @ManyToOne( fetch = FetchType.LAZY, optional = false )
    @JoinColumn( name = "sender_id", nullable = false, updatable = false )
    private UserEntity sender;

    @ManyToOne( fetch = FetchType.LAZY, optional = false )
    @JoinColumn( name = "receiver_id", nullable = false, updatable = false )
    private UserEntity receiver;

    @Enumerated( EnumType.STRING )
    @Column( name = "status", nullable = false )
    private FriendshipStatus status = FriendshipStatus.PENDING;

    @Column( name = "friend_since", nullable = true, updatable = true )
    private LocalDateTime friendSince;

    public FriendshipEntity() {}

    public FriendshipEntity( UserEntity sender, UserEntity receiver ) {

        this.sender = sender;
        this.receiver = receiver;

    }

    public Long getId() { return this.id; }
    public UserEntity getSender() { return this.sender; }
    public UserEntity getReceiver() { return this.receiver; }
    public FriendshipStatus getStatus() { return this.status; }
    public LocalDateTime getFriendSince() { return this.friendSince; }

    public void setStatus( FriendshipStatus status ) { this.status = status; }
    public void setFriendSince( LocalDateTime friendSince ) { this.friendSince = friendSince; }

}
