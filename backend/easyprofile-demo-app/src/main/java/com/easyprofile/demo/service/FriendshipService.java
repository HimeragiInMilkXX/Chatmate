package com.easyprofile.demo.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.NotAcceptableStatusException;

import com.easyprofile.authlib.entity.UserEntity;
import com.easyprofile.authlib.exception.BadRequestException;
import com.easyprofile.authlib.exception.NotFoundException;
import com.easyprofile.authlib.exception.ResourceConflictException;
import com.easyprofile.authlib.repository.UserRepository;
import com.easyprofile.authlib.service.CurrentUserProvider;
import com.easyprofile.demo.dto.FriendshipView;
import com.easyprofile.demo.entity.FriendshipEntity;
import com.easyprofile.demo.enums.FriendshipStatus;
import com.easyprofile.demo.repository.FriendshipRepository;

@Service
public class FriendshipService {
    
    @Autowired
    private FriendshipRepository friendshipRepository;
    
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private NotificationService notificationService;

    @Autowired
    private CurrentUserProvider currentUserProvider;

    @Transactional
    public FriendshipView sendFriendRequest(Long senderId, Long receiverId) {

        if (senderId.equals(receiverId)) {
            throw new BadRequestException("You cannot send a friend request to yourself");
        }

        UserEntity sender = userRepository.findById(senderId)
            .orElseThrow(() -> new NotFoundException("User not found!"));
        UserEntity receiver = userRepository.findById(receiverId)
            .orElseThrow(() -> new NotFoundException("User not found!"));

        friendshipRepository.findByTwoIds(senderId, receiverId).ifPresent(existing -> {
            throw new ResourceConflictException("Friendship request already exists");
        });

        FriendshipEntity friendshipEntity = new FriendshipEntity(sender, receiver);
        FriendshipEntity saved = friendshipRepository.save(friendshipEntity);

        notificationService.createForUser(
            receiverId,
            sender.getUsername() + " has sent a friend request to you!",
            "/user/" + sender.getId()
        );

        return toView(saved);
    }

    @Transactional
    public FriendshipView confirmFriendRequest(Long senderId, Long receiverId) {

        UserEntity receiver = userRepository.findById(receiverId)
            .orElseThrow(() -> new NotFoundException("User not found!"));

        FriendshipEntity friendship = friendshipRepository.findBySenderIdAndReceiverId(senderId, receiverId)
            .orElseThrow(() -> new NotFoundException("Friendship not found!"));

        friendship.setStatus(FriendshipStatus.ACCEPTED);
        friendship.setFriendSince(LocalDateTime.now());

        FriendshipEntity saved = friendshipRepository.save(friendship);

        notificationService.createForUser(
            senderId,
            receiver.getUsername() + " accepted your friend request!",
            "/user/" + receiver.getId()
        );

        return toView(saved);
    }

    @Transactional(readOnly = true)
    public List<FriendshipView> getNotHandledRequests(Long userId) {

        return friendshipRepository.findByReceiverIdAndStatus(userId, FriendshipStatus.PENDING)
            .stream()
            .map(this::toView)
            .toList();

    }

    @Transactional(readOnly = true)
    public List<FriendshipView> getFriends(Long userId) {

        return friendshipRepository.findByUserIdAndStatus(userId, FriendshipStatus.ACCEPTED)
            .stream()
            .map(this::toView)
            .toList();

    }

    @Transactional(readOnly = true)
    public FriendshipView getFriendship(Long firstId, Long secondId) {

        FriendshipEntity friendship = friendshipRepository.findByTwoIds(firstId, secondId)
            .orElseThrow(() -> new NotFoundException("You can send a request"));

        return toView(friendship);

    }

    public List<FriendshipView> deleteById( Long friendshipId ) {

        FriendshipEntity friendship = friendshipRepository.findById( friendshipId ).orElseThrow( () -> new NotFoundException("This friendship doesn't exist"));
        UserEntity user = currentUserProvider.getCurrentUser();

        boolean isMember = friendship.getSender().getId().equals( user.getId() ) || friendship.getReceiver().getId().equals( user.getId() );
        if( !isMember ) throw new AccessDeniedException( "You are not allowed to view messages of this chat" );

        friendshipRepository.deleteById(friendshipId);

        return getFriends(user.getId());

    }

    @Transactional( readOnly = true )
    public FriendshipView findById( Long friendshipId ) {

        FriendshipEntity friendship = friendshipRepository.findById( friendshipId ).orElseThrow( () -> new NotFoundException("This friendship doesn't exist"));
        UserEntity user = userRepository.findById( currentUserProvider.getCurrentUserId() ).orElseThrow( () -> new NotFoundException("User not found") );

        boolean isMember = friendship.getSender().getId().equals( user.getId() ) || friendship.getReceiver().getId().equals( user.getId() );
        if( !isMember ) throw new AccessDeniedException( "You are not allowed to view messages of this chat" );

        return toView( friendship );

    }

    private FriendshipView toView(FriendshipEntity entity) {

        return new FriendshipView(
            entity.getId(),
            entity.getSender().getId(),
            entity.getSender().getUsername(),
            entity.getReceiver().getId(),
            entity.getReceiver().getUsername(),
            entity.getStatus(),
            entity.getFriendSince()
        );

    }

}
