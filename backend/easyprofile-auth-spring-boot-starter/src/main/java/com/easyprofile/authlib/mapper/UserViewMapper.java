package com.easyprofile.authlib.mapper;

import com.easyprofile.authlib.dto.response.UserView;
import com.easyprofile.authlib.entity.UserEntity;

import java.util.Map;

public class UserViewMapper {

    public UserView toView(UserEntity userEntity, Map<String, Object> profileValues) {
        UserView userView = new UserView();
        userView.setId(userEntity.getId());
        userView.setUsername(userEntity.getUsername());
        userView.setEmail(userEntity.getEmail());
        userView.setAvatarUrl(userEntity.getAvatarUrl());
        userView.setLastLogin(userEntity.getLastLogin());
        userView.setCreatedAt(userEntity.getCreatedAt());
        userView.setUpdatedAt(userEntity.getUpdatedAt());
        userView.setProfile(profileValues);
        return userView;
    }
}
