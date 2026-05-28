package com.easyprofile.demo.service;

import com.easyprofile.authlib.api.DynamicProfileService;
import com.easyprofile.authlib.dto.response.UserView;
import com.easyprofile.authlib.entity.UserEntity;
import com.easyprofile.authlib.entity.UserProfileValueEntity;
import com.easyprofile.authlib.exception.BadRequestException;
import com.easyprofile.authlib.mapper.UserViewMapper;
import com.easyprofile.authlib.repository.UserRepository;
import jakarta.persistence.criteria.Expression;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import jakarta.persistence.criteria.Subquery;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;

@Service
public class UserSearchService {

    private final UserRepository userRepository;
    private final DynamicProfileService dynamicProfileService;
    private final UserViewMapper userViewMapper;

    public UserSearchService(
        UserRepository userRepository,
        DynamicProfileService dynamicProfileService,
        UserViewMapper userViewMapper
    ) {
        this.userRepository = userRepository;
        this.dynamicProfileService = dynamicProfileService;
        this.userViewMapper = userViewMapper;
    }

    @Transactional(readOnly = true)
    public Page<UserView> searchByKeyword(String keyword, Pageable pageable) {
        String normalized = normalizeKeyword(keyword);
        String likeKeyword = "%" + normalized + "%";

        Specification<UserEntity> specification = (root, query, cb) -> {
            Predicate usernameMatch = cb.like(cb.lower(root.get("username")), likeKeyword);
            Predicate dynamicMatch = buildDynamicFieldMatch(root, query.subquery(Long.class), cb, likeKeyword);
            return cb.or(usernameMatch, dynamicMatch);
        };

        return userRepository.findAll(specification, pageable)
            .map(this::toUserView);
    }

    private Predicate buildDynamicFieldMatch(
        Root<UserEntity> userRoot,
        Subquery<Long> subquery,
        jakarta.persistence.criteria.CriteriaBuilder cb,
        String likeKeyword
    ) {
        Root<UserProfileValueEntity> profileValue = subquery.from(UserProfileValueEntity.class);
        subquery.select(profileValue.get("userId"));

        List<Predicate> valueMatches = new ArrayList<>();
        valueMatches.add(cb.like(cb.lower(cb.coalesce(profileValue.get("stringValue"), "")), likeKeyword));
        valueMatches.add(cb.like(cb.lower(cb.coalesce(profileValue.get("textValue"), "")), likeKeyword));
        valueMatches.add(cb.like(cb.lower(toStringExpr(profileValue, "intValue", cb)), likeKeyword));
        valueMatches.add(cb.like(cb.lower(toStringExpr(profileValue, "longValue", cb)), likeKeyword));
        valueMatches.add(cb.like(cb.lower(toStringExpr(profileValue, "doubleValue", cb)), likeKeyword));
        valueMatches.add(cb.like(cb.lower(toStringExpr(profileValue, "booleanValue", cb)), likeKeyword));
        valueMatches.add(cb.like(cb.lower(toStringExpr(profileValue, "dateValue", cb)), likeKeyword));
        valueMatches.add(cb.like(cb.lower(toStringExpr(profileValue, "datetimeValue", cb)), likeKeyword));

        subquery.where(
            cb.equal(profileValue.get("userId"), userRoot.get("id")),
            cb.isTrue(profileValue.get("field").get("enabled")),
            cb.or(valueMatches.toArray(new Predicate[0]))
        );

        return cb.exists(subquery);
    }

    private Expression<String> toStringExpr(
        Root<UserProfileValueEntity> profileValue,
        String attributeName,
        jakarta.persistence.criteria.CriteriaBuilder cb
    ) {
        return cb.coalesce(profileValue.get(attributeName).as(String.class), "");
    }

    private String normalizeKeyword(String keyword) {
        if (keyword == null || keyword.isBlank()) {
            throw new BadRequestException("keyword is required");
        }
        return keyword.trim().toLowerCase(Locale.ROOT);
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
