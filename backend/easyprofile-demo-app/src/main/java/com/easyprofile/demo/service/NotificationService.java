package com.easyprofile.demo.service;

import com.easyprofile.authlib.entity.UserEntity;
import com.easyprofile.authlib.exception.NotFoundException;
import com.easyprofile.authlib.repository.UserRepository;
import com.easyprofile.authlib.service.CurrentUserProvider;
import com.easyprofile.demo.dto.NotificationView;
import com.easyprofile.demo.entity.NotificationEntity;
import com.easyprofile.demo.repository.NotificationRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.SecurityProperties.User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class NotificationService {

    @Autowired
    private NotificationRepository notificationRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CurrentUserProvider currentUserProvider;

    public NotificationView createForUser(Long userId, String content, String link) {
        UserEntity user = userRepository.findById(userId)
            .orElseThrow(() -> new NotFoundException("User not found"));

        NotificationEntity entity = new NotificationEntity();
        entity.setUser(user);
        entity.setContent(content.trim());
        entity.setLink(normalizeLink(link));
        entity.setConfirmed(false);
        NotificationEntity saved = notificationRepository.save(entity);

        return toView(saved);
    }

    @Transactional(readOnly = true)
    public List<NotificationView> listByUser(Long userId) {
        return notificationRepository.findByUserIdOrderByNotificationAtDesc(userId)
            .stream()
            .map(this::toView)
            .toList();
    }

    public List<NotificationView> deleteById( Long notificationId ) {

        UserEntity user = currentUserProvider.getCurrentUser();

        notificationRepository.deleteById( notificationId );

        return listByUser(user.getId());

    }

    public NotificationView confirm(Long notificationId) {
        NotificationEntity entity = notificationRepository.findById(notificationId)
            .orElseThrow(() -> new NotFoundException("Notification not found"));

        entity.setConfirmed(true);
        NotificationEntity saved = notificationRepository.save(entity);
        return toView(saved);
    }

    private NotificationView toView(NotificationEntity entity) {
        NotificationView view = new NotificationView();
        view.setId(entity.getId());
        view.setUserId(entity.getUser().getId());
        view.setContent(entity.getContent());
        view.setLink(entity.getLink());
        view.setNotificationAt(entity.getNotificationAt());
        view.setConfirmed(entity.isConfirmed());
        return view;
    }

    private String normalizeLink(String link) {
        if (link == null) {
            return null;
        }
        String trimmed = link.trim();
        return trimmed.isEmpty() ? null : trimmed;
    }
}
