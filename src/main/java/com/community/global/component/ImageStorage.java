package com.community.global.component;

import org.springframework.web.multipart.MultipartFile;

public interface ImageStorage {
    String store(MultipartFile file);

    String storePost(MultipartFile file);
}
