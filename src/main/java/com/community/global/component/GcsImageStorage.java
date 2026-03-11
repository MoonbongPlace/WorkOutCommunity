package com.community.global.component;

import com.community.global.exception.CommonException;
import com.community.global.exception.ResponseCode;
import com.google.cloud.storage.BlobInfo;
import com.google.cloud.storage.Storage;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Set;
import java.util.UUID;

@Component
@Profile("prod")
@RequiredArgsConstructor
public class GcsImageStorage implements ImageStorage {

    private static final Set<String> ALLOWED = Set.of(
            "image/jpeg",
            "image/png",
            "image/webp"
    );

    private final Storage storage;

    @Value("${app.upload.bucket}")
    private String bucket;

    @Value("${app.upload.profile-prefix}")
    private String profilePrefix;

    @Value("${app.upload.post-prefix}")
    private String postPrefix;

    @Override
    public String store(MultipartFile file) {
        return upload(file, profilePrefix);
    }

    @Override
    public String storePost(MultipartFile file) {
        return upload(file, postPrefix);
    }

    private String upload(MultipartFile file, String prefix) {
        String contentType = file.getContentType();
        validateContentType(contentType);

        String ext = extractExtension(contentType);
        String filename = UUID.randomUUID() + "." + ext;
        String objectName = prefix + "/" + filename;

        BlobInfo blobInfo = BlobInfo.newBuilder(bucket, objectName)
                .setContentType(contentType)
                .build();

        try {
            storage.create(blobInfo, file.getBytes());
        } catch (IOException e) {
            throw new CommonException(ResponseCode.FILE_UPLOAD_FAILED);
        }

        return buildObjectUrl(objectName);
    }

    private void validateContentType(String contentType) {
        if (contentType == null || !ALLOWED.contains(contentType)) {
            throw new CommonException(ResponseCode.UNSUPPORTED_FILE_TYPE);
        }
    }

    private String extractExtension(String contentType) {
        return switch (contentType) {
            case "image/jpeg" -> "jpg";
            case "image/png" -> "png";
            case "image/webp" -> "webp";
            default -> throw new CommonException(ResponseCode.UNSUPPORTED_FILE_TYPE);
        };
    }

    private String buildObjectUrl(String objectName) {
        return "https://storage.googleapis.com/" + bucket + "/" + objectName;
    }
}