package com.easyprofile.demo;

import com.easyprofile.authlib.api.DynamicProfileService;
import com.easyprofile.authlib.dto.response.ApiResponse;
import com.easyprofile.authlib.dto.response.UserView;
import com.easyprofile.authlib.entity.UserEntity;
import com.easyprofile.authlib.exception.NotFoundException;
import com.easyprofile.authlib.mapper.UserViewMapper;
import com.easyprofile.authlib.repository.UserRepository;
import com.easyprofile.authlib.exception.BadRequestException;
import com.easyprofile.demo.dto.RankedUserView;
import com.easyprofile.demo.service.FriendshipRankService;
import com.easyprofile.demo.service.UserSearchService;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;
import java.util.List;

@RestController
@RequestMapping("/user")
public class DemoUserController {

    private final UserRepository userRepository;
    private final DynamicProfileService dynamicProfileService;
    private final UserViewMapper userViewMapper;
    private final UserSearchService userSearchService;
    private final FriendshipRankService friendshipRankService;

    public DemoUserController(
        UserRepository userRepository,
        DynamicProfileService dynamicProfileService,
        UserViewMapper userViewMapper,
        UserSearchService userSearchService,
        FriendshipRankService friendshipRankService
    ) {
        this.userRepository = userRepository;
        this.dynamicProfileService = dynamicProfileService;
        this.userViewMapper = userViewMapper;
        this.userSearchService = userSearchService;
        this.friendshipRankService = friendshipRankService;
    }

    @GetMapping("/get/{id}")
    public ResponseEntity<ApiResponse<UserView>> getUserById(@PathVariable("id") Long id) {
        UserEntity user = userRepository.findById(id)
            .orElseThrow(() -> new NotFoundException("User not found"));

        Map<String, Object> profile = dynamicProfileService.getForUser(id);
        profile.remove("id");
        profile.remove("username");
        profile.remove("email");
        profile.remove("avatarUrl");
        profile.remove("lastLogin");
        profile.remove("createdAt");
        profile.remove("updatedAt");

        UserView view = userViewMapper.toView(user, profile);
        return ResponseEntity.ok(ApiResponse.success(view));
    }

    @GetMapping("/search/{keyword}")
    public ResponseEntity<ApiResponse<Page<UserView>>> searchUsers(
        @PathVariable("keyword") String keyword,
        @RequestParam(value = "page", defaultValue = "0") Integer page,
        @RequestParam(value = "item", defaultValue = "20") Integer itemPerPage
    ) {
        if (page < 0) {
            throw new BadRequestException("page must be >= 0");
        }
        if (itemPerPage <= 0 || itemPerPage > 200) {
            throw new BadRequestException("item must be between 1 and 200");
        }

        Pageable pageable = PageRequest.of(page, itemPerPage, Sort.by(Sort.Direction.DESC, "updatedAt"));
        return ResponseEntity.ok(ApiResponse.success(userSearchService.searchByKeyword(keyword, pageable)));
    }

    @GetMapping("/rank/lastlogin/{max}")
    public ResponseEntity<ApiResponse<List<UserView>>> getUsersByLastLogin(@PathVariable("max") Integer max) {
        if (max == null || max <= 0) {
            throw new BadRequestException("max must be greater than 0");
        }
        if (max > 200) {
            throw new BadRequestException("max must be <= 200");
        }

        List<UserView> users = userRepository.findAllByOrderByLastLoginDesc(PageRequest.of(0, max))
            .stream()
            .map(user -> {
                Map<String, Object> profile = dynamicProfileService.getForUser(user.getId());
                profile.remove("id");
                profile.remove("username");
                profile.remove("email");
                profile.remove("avatarUrl");
                profile.remove("lastLogin");
                profile.remove("createdAt");
                profile.remove("updatedAt");
                return userViewMapper.toView(user, profile);
            })
            .toList();

        return ResponseEntity.ok(ApiResponse.success(users));
    }

    @GetMapping("/rank/friendships")
    public ResponseEntity<ApiResponse<Page<RankedUserView>>> getUsersRankedByFriendshipCount(
        @RequestParam(value = "page", defaultValue = "0") Integer page,
        @RequestParam(value = "item", defaultValue = "20") Integer itemPerPage
    ) {
        if (page < 0) {
            throw new BadRequestException("page must be >= 0");
        }
        if (itemPerPage <= 0 || itemPerPage > 200) {
            throw new BadRequestException("item must be between 1 and 200");
        }
        Pageable pageable = PageRequest.of(page, itemPerPage);
        return ResponseEntity.ok(ApiResponse.success(friendshipRankService.getRankedUsers(pageable)));
    }
}
