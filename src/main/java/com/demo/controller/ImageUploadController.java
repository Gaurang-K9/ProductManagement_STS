package com.demo.controller;

import com.demo.model.image.ImageUploadResponse;
import com.demo.service.CloudinaryService;
import com.demo.shared.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/image")
public class ImageUploadController {

    @Autowired
    CloudinaryService cloudinaryService;

    @PostMapping(value = "/upload", consumes = "multipart/form-data")
    public ResponseEntity<ApiResponse<ImageUploadResponse>> uploadImage(@RequestPart("file") MultipartFile file){
         ImageUploadResponse response = cloudinaryService.uploadImage(file);
        return ResponseEntity.status(HttpStatus.OK).body(ApiResponse.of(response));
    }

    @DeleteMapping("/delete/{publicId}")
    public ResponseEntity<ApiResponse<Void>> deleteImage(@PathVariable String publicId){
        cloudinaryService.deleteImage(publicId);
        return ResponseEntity.status(HttpStatus.OK).body(ApiResponse.of(null));
    }
}
