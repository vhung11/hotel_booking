package com.demo.hotel_booking.controller;

import com.demo.hotel_booking.dto.request.ChangePasswordRequest;
import com.demo.hotel_booking.entity.User;
import com.demo.hotel_booking.service.ImageUploadService;
import com.demo.hotel_booking.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/v3/api-docs/users")
public class UserController {
    @Autowired
    private UserService userService;
    @Autowired
    private ImageUploadService imageUploadService;

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteUser(@PathVariable Integer id) {
        try {
            userService.deleteUserById(id);
            return ResponseEntity.ok("User deleted successfully");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("/change-password")
    public ResponseEntity<String> changePassword(
            @RequestHeader("Authorization") String token,
            @RequestBody ChangePasswordRequest request) {
        userService.changePassword(token, request);
        return ResponseEntity.ok("Password changed successfully");
    }

    @PostMapping("/send-verify-code")
    public ResponseEntity<String> sendVerificationCode(@RequestParam String email) {
        userService.sendVerificationCode(email);
        return ResponseEntity.ok("Verification code sent");
    }

    @PostMapping("/verify-code")
    public ResponseEntity<String> verifyCode(@RequestParam String email, @RequestParam String code) {
        userService.verifyCode(email, code);
        return ResponseEntity.ok("Verified successfully");
    }

    @PostMapping("/reset-password")
    public ResponseEntity<String> resetPassword(@RequestParam String email, @RequestParam String password) {
        userService.resetPassword(email, password);
        return ResponseEntity.ok("Password reset successfully");
    }

    @GetMapping("/get-user-data")
    public ResponseEntity<User> getUserData(@RequestHeader("Authorization") String token) {
        try {
            User user = userService.getUserData(token);
            return ResponseEntity.ok(user);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(null); // 400 Bad Request
        } catch (RuntimeException e) {
            return ResponseEntity.status(401).body(null); // 401 Unauthorized
        }
    }

    @PutMapping("/update")
    public ResponseEntity<User> updateUser(@RequestHeader("Authorization") String token,
                                           @RequestParam("fullName") String fullName,
                                           @RequestParam("imageFile") @Nullable MultipartFile imageFile) throws IOException {
        String imageUrl = imageUploadService.uploadImage(imageFile);
        return ResponseEntity.ok(userService.updateUser(token, fullName, imageUrl));
    }
 }
