package com.demo.hotel_booking.controller;

import com.demo.hotel_booking.service.ImageUploadService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;

@RestController
@RequestMapping("/images")
@RequiredArgsConstructor
public class ImageUploadController {

    private final ImageUploadService imageUploadService;

//    @PostMapping("/upload")
//    public ResponseEntity<Map> uploadImage(@RequestParam("file") MultipartFile file) {
//        try {
//            Map uploadResult = imageUploadService.uploadImage(file);
//            return ResponseEntity.ok(uploadResult);
//        } catch (IOException e) {
//            return ResponseEntity.status(500).body(Map.of("error", "Image upload failed"));
//        }
//    }
}