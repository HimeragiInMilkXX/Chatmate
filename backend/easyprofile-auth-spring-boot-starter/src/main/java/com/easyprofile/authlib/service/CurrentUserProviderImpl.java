package com.easyprofile.authlib.service;

import cn.dev33.satoken.stp.StpUtil;
import com.easyprofile.authlib.entity.UserEntity;
import com.easyprofile.authlib.exception.UnauthorizedException;
import com.easyprofile.authlib.repository.UserRepository;

public class CurrentUserProviderImpl implements CurrentUserProvider {

    private final UserRepository userRepository;

    public CurrentUserProviderImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public Long getCurrentUserId() {
        if (!StpUtil.isLogin()) {
            throw new UnauthorizedException("Authentication required");
        }
        return StpUtil.getLoginIdAsLong();
    }

    @Override
    public UserEntity getCurrentUser() {
        Long userId = getCurrentUserId();
        return userRepository.findById(userId)
            .orElseThrow(() -> new UnauthorizedException("Current user not found"));
    }
}
