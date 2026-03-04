package com.community.global.component;

import com.community.global.exception.CommonException;
import com.community.global.exception.ResponseCode;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Set;
import java.util.UUID;

@Component
public class ProfileImageStorage {

    private static final Set<String> ALLOWED = Set.of("image/jpeg", "image/png", "image/webp");

    @Value("${app.upload.profile-dir}")
    private String profileDir;

    @Value("${app.public.base-url}")
    private String baseUrl;

    public String store(MultipartFile file) {
        String ct = file.getContentType();
        if (ct == null || !ALLOWED.contains(ct)) {
            throw new CommonException(ResponseCode.UNSUPPORTED_FILE_TYPE);
        }

        String ext = switch (ct) {
            case "image/jpeg" -> "jpg";
            case "image/png" -> "png";
            case "image/webp" -> "webp";
            default -> throw new CommonException(ResponseCode.UNSUPPORTED_FILE_TYPE);
        };

        String filename = UUID.randomUUID() + "." + ext;

        Path dir = Paths.get(profileDir).toAbsolutePath();
        Path target = dir.resolve(filename);

        try {
            Files.createDirectories(dir);
            try (InputStream in = file.getInputStream()) {
                Files.copy(in, target, StandardCopyOption.REPLACE_EXISTING);
            }
        } catch (IOException e) {
            throw new CommonException(ResponseCode.FILE_UPLOAD_FAILED);
        }

        return baseUrl + "/uploads/profile/" + filename;
    }
}