package com.demo.hotel_booking.service;

import com.demo.hotel_booking.dto.request.ChangePasswordRequest;
import com.demo.hotel_booking.entity.User;
import com.demo.hotel_booking.repository.UserRepository;
import com.demo.hotel_booking.security.JwtService;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.Optional;
import java.util.Random;

@Service
@RequiredArgsConstructor
public class UserService {
    @Autowired
    private final UserRepository userRepository;
    @Autowired
    private final PasswordEncoder passwordEncoder;
    @Autowired
    private final JwtService jwtService;
    private final EmailService emailService;
    private ImageUploadService imageUploadService;


    public void deleteUserById(Integer userId) {
        if (!userRepository.existsById(userId)) {
            throw new IllegalArgumentException("User with ID " + userId + " does not exist");
        }
        userRepository.deleteById(userId);
    }

    public void changePassword(String token, ChangePasswordRequest request) {
        String email = jwtService.getEmailFromToken(token.substring(7));
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
        if (!passwordEncoder.matches(request.getOldPassword(), user.getPassword())) {
            throw new RuntimeException("Old password is incorrect");
        }
        user.setPassword(passwordEncoder.encode(request.getNewPassword()));
        userRepository.save(user);
    }

    public void resetPassword(String email, String newPassword) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
        if (user.getVerificationCode()!=null) {
            throw new RuntimeException("Please verify your verification code");
        }
        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);
    }

    public void sendVerificationCode(String email) {
        Optional<User> optionalUser = userRepository.findByEmail(email);
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            user.setVerificationCode(generateVerificationCode());
            user.setVerificationCodeExpiresAt(LocalDateTime.now().plusMinutes(15));
            sendVerificationEmail(user);
            userRepository.save(user);
        } else {
            throw new UsernameNotFoundException("User not found");
        }
    }

    public void verifyCode(String email, String verificationCode) {
        Optional<User> optionalUser = userRepository.findByEmail(email);
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            if (user.getVerificationCodeExpiresAt().isBefore(LocalDateTime.now())) {
                throw new RuntimeException("Verification code has expired");
            }
            if (user.getVerificationCode().equals(verificationCode)) {
                user.setVerificationCode(null);
                user.setVerificationCodeExpiresAt(null);
                userRepository.save(user);
            }
        } else {
            throw new UsernameNotFoundException("User not found");
        }
    }

    private void sendVerificationEmail(User user) {
        String subject = "Password Change Verification";
        String htmlMessage = "<html>"
                + "<body style=\"font-family: Arial, sans-serif;\">"
                + "<div style=\"background-color: #f5f5f5; padding: 20px;\">"
                + "<h2 style=\"color: #333;\">Password Change Request</h2>"
                + "<p style=\"font-size: 16px;\">To proceed with changing your password, please use the verification code below:</p>"
                + "<div style=\"background-color: #fff; padding: 20px; border-radius: 5px; box-shadow: 0 0 10px rgba(0,0,0,0.1);\">"
                + "<h3 style=\"color: #333;\">Verification Code:</h3>"
                + "<p style=\"font-size: 18px; font-weight: bold; color: #007bff;\">" + user.getVerificationCode() + "</p>"
                + "</div>"
                + "<p style=\"font-size: 14px; color: #666;\">This verification code is valid for 15 minutes.</p>"
                + "</div>"
                + "</body>"
                + "</html>";

        try {
            emailService.sendVerificationEmail(user.getEmail(), subject, htmlMessage);
        } catch (MessagingException e) {
            throw new RuntimeException("Failed to send verification email");
        }
    }

    private String generateVerificationCode() {
        Random random = new Random();
        int code = random.nextInt(900000) + 100000;
        return String.valueOf(code);
    }

    public User getUserData(String token) {
        if (token == null || !token.startsWith("Bearer ")) {
            throw new IllegalArgumentException("Invalid token format");
        }

        // Loại bỏ tiền tố "Bearer "
        String jwt = token.substring(7);

        // Trích xuất email từ JWT
        String email = jwtService.getEmailFromToken(jwt);

        // Tìm người dùng từ cơ sở dữ liệu
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found with email: " + email));
    }

    public User updateUser(String token, String fullName, String imageUrl) throws IOException {
        if (token == null || !token.startsWith("Bearer ")) {
            throw new IllegalArgumentException("Invalid token format");
        }

        String jwt = token.substring(7);
        String email = jwtService.getEmailFromToken(jwt);
        User user = userRepository.findByEmail(email).get();
        user.setFullName(fullName);
        if (!Objects.equals(imageUrl, "")) {
            user.setImageUrl(imageUrl);
        }
        return userRepository.save(user);
    }
}
