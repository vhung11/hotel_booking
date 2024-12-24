package com.demo.hotel_booking.controller;

import com.demo.hotel_booking.dto.request.ChangePasswordRequest;
import com.demo.hotel_booking.service.UserDetailsServiceImpl;
import com.demo.hotel_booking.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin(origins = "http://localhost:3000", allowedHeaders = "*")
@RequestMapping("/v3/api-docs/users")
public class UserController {
    @Autowired
    private UserService service;

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteUser(@PathVariable Integer id) {
        try {
            service.deleteUserById(id);
            return ResponseEntity.ok("User deleted successfully");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("/change-password")
    public ResponseEntity<String> changePassword(
            @RequestHeader("Authorization") String token,
            @RequestBody ChangePasswordRequest request) {
        service.changePassword(token, request);
        return ResponseEntity.ok("Password changed successfully");
    }

    @PostMapping("/send-verify-code")
    public ResponseEntity<String> sendVerificationCode(@RequestParam String email) {
        service.sendVerificationCode(email);
        return ResponseEntity.ok("Verification code sent");
    }

    @PostMapping("/verify-code")
    public ResponseEntity<String> verifyCode(@RequestParam String email, @RequestParam String code) {
        service.verifyCode(email, code);
        return ResponseEntity.ok("Verified successfully");
    }

    @PostMapping("/reset-password")
    public ResponseEntity<String> resetPassword(@RequestParam String email, @RequestParam String password) {
        service.resetPassword(email, password);
        return ResponseEntity.ok("Password reset successfully");
    }
}
