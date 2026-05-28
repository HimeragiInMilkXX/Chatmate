package com.easyprofile.demo.repository;

import com.easyprofile.demo.entity.NotificationEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface NotificationRepository extends JpaRepository<NotificationEntity, Long> {

    List<NotificationEntity> findByUserIdOrderByNotificationAtDesc(Long userId);
}
