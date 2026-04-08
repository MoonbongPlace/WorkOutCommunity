package com.community.global.component;

import com.google.cloud.storage.Storage;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.util.Set;

@Component
@Profile("prod")
@RequiredArgsConstructor
public class AwsImageStorage implements ImageStorage{

    private static final Set<String> ALLOWED = Set.of(
            "image/jpeg",
            "image/png",
            "image/webp"
    );

    private final Storage storage;

    @Value("${app.s3.bucket}")
    private String bucket;

    @Value("${app.upload.profile-prefix}")
    private String profilePrefix;

    @Value("${app.upload.post-prefix}")
    private String postPrefix;

    @Override
    public String store(MultipartFile file) {
        return "";
    }

    @Override
    public String storePost(MultipartFile file) {
        return "";
    }
}
