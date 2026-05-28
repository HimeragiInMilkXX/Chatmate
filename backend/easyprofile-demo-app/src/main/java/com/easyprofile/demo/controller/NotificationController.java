package com.easyprofile.demo.controller;

import com.easyprofile.authlib.dto.response.ApiResponse;
import com.easyprofile.demo.dto.CreateNotificationRequest;
import com.easyprofile.demo.dto.NotificationView;
import com.easyprofile.demo.service.NotificationService;
import jakarta.validation.Valid;

import org.apache.catalina.connector.Response;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/notification")
public class NotificationController {

    private final NotificationService notificationService;

    public NotificationController(NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    @PostMapping("/create")
    public ResponseEntity<ApiResponse<NotificationView>> create(@Valid @RequestBody CreateNotificationRequest request) {
        System.out.println( request.toString() );
        NotificationView view = notificationService.createForUser(request.getUserId(), request.getContent(), request.getLink());
        return ResponseEntity.ok(ApiResponse.success("notification created", view));
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<ApiResponse<List<NotificationView>>> listByUser(@PathVariable("userId") Long userId) {
        return ResponseEntity.ok(ApiResponse.success(notificationService.listByUser(userId)));
    }

    @PatchMapping("/confirm/{id}")
    public ResponseEntity<ApiResponse<NotificationView>> confirm(@PathVariable("id") Long id) {
        return ResponseEntity.ok(ApiResponse.success("notification confirmed", notificationService.confirm(id)));
    }

    @DeleteMapping("/delete/{notificationId}")
    public ResponseEntity<ApiResponse<List<NotificationView>>> delete( @PathVariable("notificationId") Long notificationId ) {
        return ResponseEntity.ok( ApiResponse.success( notificationService.deleteById(notificationId) ) );
    }

}
