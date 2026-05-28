package com.easyprofile.authlib.controller;

import cn.dev33.satoken.annotation.SaCheckLogin;
import com.easyprofile.authlib.dto.request.LoginRequest;
import com.easyprofile.authlib.dto.request.RegisterRequest;
import com.easyprofile.authlib.dto.request.ResetPasswordRequest;
import com.easyprofile.authlib.dto.request.UpdateProfileRequest;
import com.easyprofile.authlib.dto.response.ApiResponse;
import com.easyprofile.authlib.dto.response.AuthTokenResponse;
import com.easyprofile.authlib.dto.response.AvatarUploadResponse;
import com.easyprofile.authlib.dto.response.UserView;
import com.easyprofile.authlib.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/register")
    public ResponseEntity<ApiResponse<AuthTokenResponse>> register(@Valid @RequestBody RegisterRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
            .body(ApiResponse.success("registered", authService.register(request)));
    }

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<AuthTokenResponse>> login(@Valid @RequestBody LoginRequest request) {
        return ResponseEntity.ok(ApiResponse.success("logged in", authService.login(request)));
    }

    @SaCheckLogin
    @GetMapping("/me")
    public ResponseEntity<ApiResponse<AuthTokenResponse>> me() {
        return ResponseEntity.ok(ApiResponse.success(authService.me()));
    }

    @SaCheckLogin
    @PostMapping(value = "/avatar", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ApiResponse<AvatarUploadResponse>> uploadAvatar(@RequestPart("file") MultipartFile file) {
        return ResponseEntity.ok(ApiResponse.success("avatar updated", authService.uploadAvatar(file)));
    }

    @SaCheckLogin
    @PatchMapping("/update")
    public ResponseEntity<ApiResponse<UserView>> update(@Valid @RequestBody UpdateProfileRequest request) {
        return ResponseEntity.ok(ApiResponse.success("profile updated", authService.updateProfile(request)));
    }

    @SaCheckLogin
    @PostMapping("/resetPassword")
    public ResponseEntity<ApiResponse<AuthTokenResponse>> resetPassword(@Valid @RequestBody ResetPasswordRequest request) {
        return ResponseEntity.ok(ApiResponse.success("password reset", authService.resetPassword(request)));
    }

    @SaCheckLogin
    @PostMapping("/logout")
    public ResponseEntity<ApiResponse<Void>> logout() {
        authService.logout();
        return ResponseEntity.ok(ApiResponse.success("logout success", null));
    }
}
