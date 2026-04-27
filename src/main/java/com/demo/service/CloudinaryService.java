package com.demo.service;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.demo.model.image.ImageUploadResponse;
import com.demo.util.FileType;
import com.demo.util.FileValidationUtility;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class CloudinaryService {

    @Autowired
    private Cloudinary cloudinary;

    public ImageUploadResponse uploadImage(MultipartFile file){

        FileValidationUtility.validate(file, FileType.IMAGE);

        try {
            var uploadResult = cloudinary.uploader().upload(
                    file.getBytes(),
                    ObjectUtils.asMap("folder", "pm_assets")
            );

            String imageUrl = uploadResult.get("secure_url").toString();
            String publicId = uploadResult.get("public_id").toString();

            return new ImageUploadResponse(imageUrl, publicId);
        }
        catch (Exception e) {
            throw new RuntimeException("Failed to upload image");
        }
    }

    public void deleteImage(String publicId) {
        try {
            cloudinary.uploader().destroy(
                    publicId,
                    ObjectUtils.asMap("invaldidate", true)
            );
        } catch (Exception e) {
            throw new RuntimeException("Failed to delete image", e);
        }
    }
}
