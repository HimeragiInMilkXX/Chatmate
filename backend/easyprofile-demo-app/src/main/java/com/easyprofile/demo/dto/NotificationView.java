package com.easyprofile.demo.dto;

import java.time.LocalDateTime;

public class NotificationView {

    private Long id;
    private Long userId;
    private String content;
    private String link;
    private LocalDateTime notificationAt;
    private boolean confirmed;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public LocalDateTime getNotificationAt() {
        return notificationAt;
    }

    public void setNotificationAt(LocalDateTime notificationAt) {
        this.notificationAt = notificationAt;
    }

    public boolean isConfirmed() {
        return confirmed;
    }

    public void setConfirmed(boolean confirmed) {
        this.confirmed = confirmed;
    }
}
