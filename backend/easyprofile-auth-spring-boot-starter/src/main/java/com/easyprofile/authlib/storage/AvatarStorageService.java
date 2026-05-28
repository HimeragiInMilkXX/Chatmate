package com.easyprofile.authlib.storage;

import org.springframework.web.multipart.MultipartFile;

public interface AvatarStorageService {

    AvatarFile store(MultipartFile file);
}
