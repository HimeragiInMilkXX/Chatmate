package com.easyprofile.demo.dto;

import com.easyprofile.authlib.dto.response.UserView;

public class RankedUserView {

    private UserView user;
    private long friendshipCount;

    public RankedUserView() {
    }

    public RankedUserView(UserView user, long friendshipCount) {
        this.user = user;
        this.friendshipCount = friendshipCount;
    }

    public UserView getUser() {
        return user;
    }

    public void setUser(UserView user) {
        this.user = user;
    }

    public long getFriendshipCount() {
        return friendshipCount;
    }

    public void setFriendshipCount(long friendshipCount) {
        this.friendshipCount = friendshipCount;
    }
}
