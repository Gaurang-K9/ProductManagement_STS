package com.demo.util;

import com.demo.exception.BadRequestException;
import org.springframework.web.multipart.MultipartFile;

public class FileValidationUtility {

    public static void validate(MultipartFile file, FileType fileType) {

        if (file == null || file.isEmpty()) {
            throw new BadRequestException("File cannot be empty");
        }

        switch (fileType) {
            case IMAGE -> validateImage(file, 3);
            case PROFILE_IMAGE -> validateImage(file, 1);
        }
    }

    private static void validateImage(MultipartFile file, int maxSizeMB) {
        String contentType = file.getContentType();

        if (contentType == null || !contentType.startsWith("image/")) {
            throw new BadRequestException("Only image files are allowed");
        }

        validateSize(file, maxSizeMB);
    }

    private static void validateSize(MultipartFile file, int maxSizeMB) {
        long maxBytes = maxSizeMB * 1024L * 1024L;

        if (file.getSize() > maxBytes) {
            throw new BadRequestException("File size must be less than " + maxSizeMB + "MB");
        }
    }
}