package com.easyprofile.authlib.service;

import cn.dev33.satoken.stp.SaLoginModel;
import cn.dev33.satoken.stp.StpUtil;
import com.easyprofile.authlib.autoconfigure.AuthLibProperties;
import com.easyprofile.authlib.dto.request.LoginRequest;
import com.easyprofile.authlib.dto.request.RegisterRequest;
import com.easyprofile.authlib.dto.request.ResetPasswordRequest;
import com.easyprofile.authlib.dto.request.UpdateProfileRequest;
import com.easyprofile.authlib.dto.response.AuthTokenResponse;
import com.easyprofile.authlib.dto.response.AvatarUploadResponse;
import com.easyprofile.authlib.dto.response.UserView;
import com.easyprofile.authlib.entity.UserEntity;
import com.easyprofile.authlib.exception.BadRequestException;
import com.easyprofile.authlib.exception.ResourceConflictException;
import com.easyprofile.authlib.exception.UnauthorizedException;
import com.easyprofile.authlib.mapper.UserViewMapper;
import com.easyprofile.authlib.repository.UserRepository;
import com.easyprofile.authlib.storage.AvatarFile;
import com.easyprofile.authlib.storage.AvatarStorageService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

@Transactional
public class AuthService {

    private static final Set<String> ALLOWED_IMAGE_CONTENT_TYPES = Set.of(
        "image/jpeg", "image/png", "image/webp", "image/gif"
    );

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;
    private final CurrentUserProvider currentUserProvider;
    private final DynamicProfileServiceImpl dynamicProfileService;
    private final AvatarStorageService avatarStorageService;
    private final UserViewMapper userViewMapper;
    private final AuthLibProperties authLibProperties;

    public AuthService(
        UserRepository userRepository,
        BCryptPasswordEncoder passwordEncoder,
        CurrentUserProvider currentUserProvider,
        DynamicProfileServiceImpl dynamicProfileService,
        AvatarStorageService avatarStorageService,
        UserViewMapper userViewMapper,
        AuthLibProperties authLibProperties
    ) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.currentUserProvider = currentUserProvider;
        this.dynamicProfileService = dynamicProfileService;
        this.avatarStorageService = avatarStorageService;
        this.userViewMapper = userViewMapper;
        this.authLibProperties = authLibProperties;
    }

    public AuthTokenResponse register(RegisterRequest request) {
        if (!request.getPassword().equals(request.getConfirmPassword())) {
            throw new BadRequestException("Password and confirmPassword do not match");
        }

        String username = request.getUsername().trim();
        String email = request.getEmail().trim().toLowerCase(Locale.ROOT);

        if (userRepository.existsByUsername(username)) {
            throw new ResourceConflictException("Username already exists");
        }

        if (userRepository.existsByEmail(email)) {
            throw new ResourceConflictException("Email already exists");
        }

        UserEntity user = new UserEntity();
        user.setUsername(username);
        user.setEmail(email);
        user.setPasswordHash(passwordEncoder.encode(request.getPassword()));
        user = userRepository.save(user);

        String token = issueToken(user);
        return new AuthTokenResponse(toUserView(user), token);
    }

    public AuthTokenResponse login(LoginRequest request) {
        String usernameOrEmail = request.getUsernameOrEmail().trim();
        UserEntity user = userRepository.findByUsernameOrEmail(usernameOrEmail, usernameOrEmail)
            .orElseThrow(() -> new UnauthorizedException("Invalid credentials"));

        if (!passwordEncoder.matches(request.getPassword(), user.getPasswordHash())) {
            throw new UnauthorizedException("Invalid credentials");
        }

        String token = issueToken(user);
        return new AuthTokenResponse(toUserView(user), token);
    }

    @Transactional(readOnly = true)
    public AuthTokenResponse me() {
        UserEntity user = currentUserProvider.getCurrentUser();
        String token = StpUtil.getTokenValue();
        return new AuthTokenResponse(toUserView(user), token);
    }

    public AvatarUploadResponse uploadAvatar(MultipartFile file) {
        validateAvatarFile(file);

        UserEntity user = currentUserProvider.getCurrentUser();
        AvatarFile avatarFile = avatarStorageService.store(file);
        user.setAvatarUrl(avatarFile.getUrl());
        UserEntity saved = userRepository.save(user);

        return new AvatarUploadResponse(avatarFile.getUrl(), toUserView(saved));
    }

    public UserView updateProfile(UpdateProfileRequest request) {
        UserEntity user = currentUserProvider.getCurrentUser();

        if (request.getUsername() != null) {
            String nextUsername = request.getUsername().trim();
            if (!nextUsername.equals(user.getUsername()) && userRepository.existsByUsernameAndIdNot(nextUsername, user.getId())) {
                throw new ResourceConflictException("Username already exists");
            }
            user.setUsername(nextUsername);
        }

        if (request.getEmail() != null) {
            String nextEmail = request.getEmail().trim().toLowerCase(Locale.ROOT);
            if (!nextEmail.equals(user.getEmail()) && userRepository.existsByEmailAndIdNot(nextEmail, user.getId())) {
                throw new ResourceConflictException("Email already exists");
            }
            user.setEmail(nextEmail);
        }

        userRepository.save(user);

        if (!request.getDynamicFields().isEmpty()) {
            dynamicProfileService.setForCurrentUser(request.getDynamicFields());
        }

        return toUserView(user);
    }

    public AuthTokenResponse resetPassword(ResetPasswordRequest request) {
        if (!request.getNewPassword().equals(request.getConfirmPassword())) {
            throw new BadRequestException("newPassword and confirmPassword do not match");
        }

        UserEntity user = currentUserProvider.getCurrentUser();
        if (authLibProperties.getSecurity().isRequireOldPasswordOnReset()) {
            if (request.getOldPassword() == null || request.getOldPassword().isBlank()) {
                throw new BadRequestException("oldPassword is required");
            }
            if (!passwordEncoder.matches(request.getOldPassword(), user.getPasswordHash())) {
                throw new UnauthorizedException("oldPassword is incorrect");
            }
        }

        int cooldownDays = authLibProperties.getResetPassword().getCooldownDays();
        LocalDateTime now = LocalDateTime.now();
        if (user.getLastPasswordResetAt() != null
            && user.getLastPasswordResetAt().plusDays(cooldownDays).isAfter(now)) {
            throw new BadRequestException("Password can only be reset once every " + cooldownDays + " days");
        }

        user.setPasswordHash(passwordEncoder.encode(request.getNewPassword()));
        user.setLastPasswordResetAt(now);
        user = userRepository.save(user);

        StpUtil.logout(user.getId());
        String token = issueToken(user);
        return new AuthTokenResponse(toUserView(user), token);
    }

    public void logout() {
        StpUtil.logout();
    }

    private String issueToken(UserEntity user) {
        user.setLastLogin(LocalDateTime.now());
        userRepository.save(user);

        Long userId = user.getId();
        StpUtil.login(userId, SaLoginModel.create()
            .setDevice("default")
            .setTimeout(authLibProperties.getTokenTimeout()));
        return StpUtil.getTokenValue();
    }

    private UserView toUserView(UserEntity user) {
        Map<String, Object> profile = dynamicProfileService.getForUser(user.getId());
        profile.remove("id");
        profile.remove("username");
        profile.remove("email");
        profile.remove("avatarUrl");
        profile.remove("lastLogin");
        profile.remove("createdAt");
        profile.remove("updatedAt");
        return userViewMapper.toView(user, profile);
    }

    private void validateAvatarFile(MultipartFile file) {
        if (file == null || file.isEmpty()) {
            throw new BadRequestException("Avatar file is required");
        }

        if (file.getSize() > authLibProperties.getAvatar().getMaxSizeBytes()) {
            throw new BadRequestException("Avatar file exceeds max size");
        }

        String contentType = file.getContentType();
        if (contentType == null || !ALLOWED_IMAGE_CONTENT_TYPES.contains(contentType.toLowerCase(Locale.ROOT))) {
            throw new BadRequestException("Unsupported avatar content type");
        }
    }
}
