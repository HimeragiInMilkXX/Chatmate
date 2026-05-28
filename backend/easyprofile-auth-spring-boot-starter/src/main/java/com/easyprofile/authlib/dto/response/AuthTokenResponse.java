package com.easyprofile.authlib.dto.response;

public class AuthTokenResponse {

    private UserView user;
    private String token;

    public AuthTokenResponse(UserView user, String token) {
        this.user = user;
        this.token = token;
    }

    public UserView getUser() {
        return user;
    }

    public void setUser(UserView user) {
        this.user = user;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
