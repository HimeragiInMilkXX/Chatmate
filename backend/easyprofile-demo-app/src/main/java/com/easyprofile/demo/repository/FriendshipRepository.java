package com.easyprofile.demo.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.easyprofile.demo.entity.FriendshipEntity;
import com.easyprofile.demo.enums.FriendshipStatus;
import com.easyprofile.demo.repository.projection.UserFriendshipCountProjection;

public interface FriendshipRepository extends JpaRepository<FriendshipEntity, Long> {

    @Query("""

        select e from FriendshipEntity e
        where ( e.sender.id = :user_id or e.receiver.id = :user_id )
            and e.status = :status

    """)
    List<FriendshipEntity> findByUserIdAndStatus(

        @Param("user_id") Long userId,
        @Param( "status" ) FriendshipStatus status

    );

    @Query("""

        select e from FriendshipEntity e
        where ( e.sender.id = :first_id and e.receiver.id = :second_id )
            or ( e.sender.id = :second_id and e.receiver.id = :first_id )

    """ )
    Optional<FriendshipEntity> findByTwoIds(

        @Param( "first_id" ) Long firstId,
        @Param( "second_id" ) Long secondId

    );

    List<FriendshipEntity> findByReceiverIdAndStatus( Long receiverId, FriendshipStatus status );

    Optional<FriendshipEntity> findBySenderIdAndReceiverId( Long senderId, Long receiverId );

    /*

        1. Start from users

        2. For each user, attach matching friendship rows
        status = ACCEPTED
        and user is sender or receiver

        3. This creates multiple rows for one user if they have multiple friendships

        4. GROUP BY u.id + u.last_login
        compress those multiple rows back into one row per user

        5. COUNT(f.id)
        count how many friendship rows were attached to that user

        6. ORDER BY
        highest friendshipCount first,
        newest last_login second,
        smallest user id third

    */

    @Query(
        value = """
            SELECT
                u.id AS userId,
                COUNT(f.id) AS friendshipCount
            FROM users u
            LEFT JOIN friendships f
                ON f.status = 'ACCEPTED'
               AND (f.sender_id = u.id OR f.receiver_id = u.id)
            GROUP BY u.id, u.last_login
            ORDER BY friendshipCount DESC, u.last_login DESC, u.id ASC
            """,
        countQuery = "SELECT COUNT(*) FROM users",
        nativeQuery = true
    )
    Page<UserFriendshipCountProjection> findUserFriendshipRanks(Pageable pageable);

}
