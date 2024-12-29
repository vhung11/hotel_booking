package com.demo.hotel_booking.service;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class ImageUploadService {

    @Autowired
    private Cloudinary cloudinary;

    public String uploadImage(MultipartFile file) throws IOException {
        if (file.isEmpty()) {
            return "";
        }
        // Upload file to Cloudinary
        Map<String, Object> uploadResult = cloudinary.uploader().upload(file.getBytes(), ObjectUtils.emptyMap());

        // Extract and return the URL from the upload result
        return uploadResult.get("url").toString();
    }

    public List<String> uploadImages(List<MultipartFile> files) throws IOException {
        List<String> imageUrls = new ArrayList<>();

        for (MultipartFile file : files) {
            // Sử dụng lại phương thức uploadImage để upload từng file
            String imageUrl = uploadImage(file);
            if (!imageUrl.isEmpty()) {
                imageUrls.add(imageUrl);
            }
        }

        return imageUrls;
    }

}