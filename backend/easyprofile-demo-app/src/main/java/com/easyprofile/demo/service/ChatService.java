package com.easyprofile.demo.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.NotAcceptableStatusException;

import com.easyprofile.authlib.entity.UserEntity;
import com.easyprofile.authlib.exception.NotFoundException;
import com.easyprofile.authlib.repository.UserRepository;
import com.easyprofile.authlib.service.CurrentUserProvider;
import com.easyprofile.demo.dto.ChatMessageRequest;
import com.easyprofile.demo.dto.ChatMessageView;
import com.easyprofile.demo.entity.FriendshipEntity;
import com.easyprofile.demo.entity.MessageEntity;
import com.easyprofile.demo.repository.FriendshipRepository;
import com.easyprofile.demo.repository.MessageRepository;

@Service
public class ChatService {
    
    @Autowired
    private FriendshipRepository friendshipRepository;

    @Autowired
    private MessageRepository messageRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CurrentUserProvider currentUserProvider;

    public ChatMessageView saveMessage( ChatMessageRequest request, String userId ) {
        Long currentUserId;
        try {
            currentUserId = Long.valueOf(userId);
        } catch (NumberFormatException ex) {
            throw new NotFoundException("User not found!");
        }

        UserEntity currentUser = userRepository.findById(currentUserId).orElseThrow(() -> new NotFoundException("User not found!"));
        FriendshipEntity friendship = friendshipRepository.findById( request.getFriendshipId() ).orElseThrow( () -> new NotFoundException( "Friendship not found!" ) );

        boolean isMember = friendship.getSender().getId().equals( currentUser.getId() ) || friendship.getReceiver().getId().equals( currentUser.getId() );

        if( !isMember ) throw new NotAcceptableStatusException( "You are not allowed to send message to this chat" );

        MessageEntity message = new MessageEntity( friendship, currentUser, request.getContent() );
        MessageEntity saved = messageRepository.save( message );

        return toView( saved );

    }

    @Transactional(readOnly = true)
    public List<ChatMessageView> getMessages( Long friendshipId ) {

        UserEntity currentUser = userRepository.findById( currentUserProvider.getCurrentUserId() ).orElseThrow( () -> new NotFoundException( "User not found!" ) );
        FriendshipEntity friendship = friendshipRepository.findById( friendshipId ).orElseThrow( () -> new NotFoundException( "Friendship not found!" ) );
        
        boolean isMember = friendship.getSender().getId().equals( currentUser.getId() ) || friendship.getReceiver().getId().equals( currentUser.getId() );

        if( !isMember ) throw new NotAcceptableStatusException( "You are not allowed to view messages of this chat" );

        return messageRepository.findByFriendshipIdWithSender(friendshipId)
            .stream()
            .map( this::toView )
            .toList();
            
    }

    private ChatMessageView toView( MessageEntity message ) {

        return new ChatMessageView(

            message.getId(),
            message.getFriendship().getId(),
            message.getSender().getId(),
            message.getSender().getUsername(),
            message.getContent(),
            message.getCreatedAt()

        );

    }

}
