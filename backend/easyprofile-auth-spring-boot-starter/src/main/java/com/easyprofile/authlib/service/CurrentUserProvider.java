package com.easyprofile.authlib.service;

import com.easyprofile.authlib.entity.UserEntity;

public interface CurrentUserProvider {

    Long getCurrentUserId();

    UserEntity getCurrentUser();
}
