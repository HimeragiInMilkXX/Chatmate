package com.easyprofile.demo.service;

import com.easyprofile.authlib.api.DynamicProfileService;
import com.easyprofile.authlib.dto.response.UserView;
import com.easyprofile.authlib.entity.UserEntity;
import com.easyprofile.authlib.mapper.UserViewMapper;
import com.easyprofile.authlib.repository.UserRepository;
import com.easyprofile.demo.dto.RankedUserView;
import com.easyprofile.demo.repository.FriendshipRepository;
import com.easyprofile.demo.repository.projection.UserFriendshipCountProjection;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class FriendshipRankService {

    private final FriendshipRepository friendshipRepository;
    private final UserRepository userRepository;
    private final DynamicProfileService dynamicProfileService;
    private final UserViewMapper userViewMapper;

    public FriendshipRankService(
        FriendshipRepository friendshipRepository,
        UserRepository userRepository,
        DynamicProfileService dynamicProfileService,
        UserViewMapper userViewMapper
    ) {
        this.friendshipRepository = friendshipRepository;
        this.userRepository = userRepository;
        this.dynamicProfileService = dynamicProfileService;
        this.userViewMapper = userViewMapper;
    }

    @Transactional(readOnly = true)
    public Page<RankedUserView> getRankedUsers(Pageable pageable) {
        Page<UserFriendshipCountProjection> ranks = friendshipRepository.findUserFriendshipRanks(pageable);
        List<Long> userIds = ranks.getContent().stream()
            .map(UserFriendshipCountProjection::getUserId)
            .toList();

        Map<Long, UserEntity> usersById = userRepository.findAllById(userIds).stream()
            .collect(Collectors.toMap(UserEntity::getId, Function.identity()));

        return ranks.map(rank -> {
            UserEntity user = usersById.get(rank.getUserId());
            UserView userView = toUserView(user);
            long count = rank.getFriendshipCount() == null ? 0L : rank.getFriendshipCount();
            return new RankedUserView(userView, count);
        });
    }

    private UserView toUserView(UserEntity user) {
        Map<String, Object> profile = dynamicProfileService.getForUser(user.getId());
        profile.remove("id");
        profile.remove("username");
        profile.remove("email");
        profile.remove("avatarUrl");
        profile.remove("lastLogin");
        profile.remove("createdAt");
        profile.remove("updatedAt");
        return userViewMapper.toView(user, profile);
    }
}
