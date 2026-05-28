package com.easyprofile.authlib.dto.response;

public class AvatarUploadResponse {

    private String avatarUrl;
    private UserView user;

    public AvatarUploadResponse(String avatarUrl, UserView user) {
        this.avatarUrl = avatarUrl;
        this.user = user;
    }

    public String getAvatarUrl() {
        return avatarUrl;
    }

    public void setAvatarUrl(String avatarUrl) {
        this.avatarUrl = avatarUrl;
    }

    public UserView getUser() {
        return user;
    }

    public void setUser(UserView user) {
        this.user = user;
    }
}
