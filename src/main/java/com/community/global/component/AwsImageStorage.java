package com.community.global.component;

import com.community.global.exception.CommonException;
import com.community.global.exception.ResponseCode;
import com.google.cloud.storage.BlobInfo;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Set;
import java.util.UUID;

@Component
@Profile("aws")
@RequiredArgsConstructor
public class AwsImageStorage implements ImageStorage{

    private static final Set<String> ALLOWED = Set.of(
            "image/jpeg",
            "image/png",
            "image/webp"
    );

    private final S3Client s3Client;

    @Value("${app.s3.bucket}")
    private String bucket;

    @Value("${app.s3.region}")
    private String region;

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

        PutObjectRequest request = PutObjectRequest.builder()
                .bucket(bucket)
                .key(objectName)
                .contentType(contentType)
                .build();

        try {
            s3Client.putObject(request, RequestBody.fromBytes(file.getBytes()));
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
        return "https://" + bucket + ".s3." + region + ".amazonaws.com/" + objectName;
    }
}
