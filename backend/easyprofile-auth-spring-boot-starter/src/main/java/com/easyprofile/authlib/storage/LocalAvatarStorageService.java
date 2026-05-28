package com.easyprofile.authlib.storage;

import com.easyprofile.authlib.autoconfigure.AuthLibProperties;
import com.easyprofile.authlib.exception.BadRequestException;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Locale;
import java.util.UUID;

public class LocalAvatarStorageService implements AvatarStorageService {

    private final AuthLibProperties authLibProperties;

    public LocalAvatarStorageService(AuthLibProperties authLibProperties) {
        this.authLibProperties = authLibProperties;
    }

    @Override
    public AvatarFile store(MultipartFile file) {
        String originalFilename = file.getOriginalFilename();
        String extension = StringUtils.getFilenameExtension(originalFilename);
        String safeExtension = extension == null ? "bin" : extension.toLowerCase(Locale.ROOT);

        String storedName = UUID.randomUUID() + "." + safeExtension;
        Path uploadDir = Paths.get(authLibProperties.getAvatar().getUploadDir()).toAbsolutePath().normalize();
        Path targetPath = uploadDir.resolve(storedName).normalize();

        if (!targetPath.startsWith(uploadDir)) {
            throw new BadRequestException("Invalid upload path");
        }

        try {
            Files.createDirectories(uploadDir);
            Files.copy(file.getInputStream(), targetPath, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException ex) {
            throw new BadRequestException("Failed to store avatar file");
        }

        String baseUrl = authLibProperties.getAvatar().getBaseUrl();
        if (!baseUrl.endsWith("/")) {
            baseUrl = baseUrl + "/";
        }

        return new AvatarFile(storedName, baseUrl + storedName);
    }
}
