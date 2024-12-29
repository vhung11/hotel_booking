package com.demo.hotel_booking.controller;

import com.demo.hotel_booking.service.ImageUploadService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;

@RestController
@RequestMapping("/v3/api-docs/images")
@RequiredArgsConstructor
public class ImageUploadController {

    private final ImageUploadService imageUploadService;

    @PostMapping("/upload")
    public ResponseEntity<String> uploadImage(@RequestParam("file") MultipartFile file) {
        try {
            String uploadResult = imageUploadService.uploadImage(file);
            return ResponseEntity.ok(uploadResult);
        } catch (IOException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}