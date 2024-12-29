package com.demo.hotel_booking.service;

import com.demo.hotel_booking.dto.request.HotelRegistrationRequest;
import com.demo.hotel_booking.entity.Hotel;
import com.demo.hotel_booking.entity.Room;
import com.demo.hotel_booking.entity.User;
import com.demo.hotel_booking.repository.HotelRepository;
import com.demo.hotel_booking.repository.RoleRepository;
import com.demo.hotel_booking.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class HotelService {

    private final HotelRepository hotelRepository;

    private final RoleRepository roleRepository;

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    public HotelService(RoleRepository roleRepository, UserRepository userRepository, PasswordEncoder passwordEncoder, HotelRepository hotelRepository) {
        this.roleRepository = roleRepository;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.hotelRepository = hotelRepository;
    }

//    public List<Hotel> findHotelsWithAvailableRooms(String address, LocalDate checkInDate, LocalDate checkOutDate, int numOfAdults, int numOfChildren) {
//        return hotelRepository.findHotelsWithAvailableRooms(address, checkInDate, checkOutDate, numOfAdults, numOfChildren);
//    }

    public List<Room> findAvailableRooms(Long hotelId, LocalDate checkInDate, LocalDate checkOutDate, int numOfAdults, int numOfChildren) {
        return hotelRepository.findAvailableRooms(hotelId, checkInDate, checkOutDate, numOfAdults, numOfChildren);
    }

    public List<Room> findRoomsByHotelId(Long hotelId) {
        return hotelRepository.findById(hotelId).orElseThrow().getRooms();
    }

    public void hotelRegister(HotelRegistrationRequest request) {
        // Create and save the User
        var userRole = roleRepository.findByName("MANAGER")
                .orElseThrow(() -> new IllegalStateException("ROLE MANAGER was not initialized"));

        var user = User.builder()
                .fullName(request.getEmail())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .accountLocked(false)
                .verificationCode(null)
                .verificationCodeExpiresAt(null)
                .enabled(true)
                .roles(List.of(userRole))
                .build();
        userRepository.save(user);

        var hotel = Hotel.builder()
                .email(request.getEmail())
                .password(request.getPassword())
                .name(request.getName())
                .province(request.getProvince())
                .district(request.getDistrict())
                .ward(request.getWard())
                .latitude(request.getLatitude())
                .longitude(request.getLongitude())
                .type(request.getType())
                .build();
        hotelRepository.save(hotel);
    }

    public Hotel findHotelByEmail(String email) {
        return this.hotelRepository.findAll().stream().filter(hotel -> hotel.getEmail().equals(email)).findFirst().orElse(null);
    }

}