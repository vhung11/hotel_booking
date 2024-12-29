package com.demo.hotel_booking.repository;

import com.demo.hotel_booking.entity.User;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;


public interface UserRepository extends JpaRepository<User, Integer> {

    Optional<User> findByEmail(String email);

    boolean existsByEmail(
            @Email(message = "Email is not well formatted")
            @NotEmpty(message = "Email is mandatory")
            @NotNull(message = "Email is mandatory")
            String email);
}
