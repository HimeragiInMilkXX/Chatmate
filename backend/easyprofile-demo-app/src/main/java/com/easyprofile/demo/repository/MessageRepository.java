package com.easyprofile.demo.repository;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.easyprofile.demo.entity.MessageEntity;

public interface MessageRepository extends JpaRepository<MessageEntity, Long> {

    @Query("""
        select m
        from MessageEntity m
        join fetch m.sender
        join fetch m.friendship
        where m.friendship.id = :friendshipId
        order by m.createdAt asc
    """)
    List<MessageEntity> findByFriendshipIdWithSender(@Param("friendshipId") Long friendshipId);

}
