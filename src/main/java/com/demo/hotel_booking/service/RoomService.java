package com.demo.hotel_booking.service;

import com.demo.hotel_booking.dto.request.RoomInfo;
import com.demo.hotel_booking.entity.Hotel;
import com.demo.hotel_booking.entity.Room;
import com.demo.hotel_booking.repository.RoomRepository;
import com.demo.hotel_booking.security.JwtService;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Service
public class RoomService {
    private final RoomRepository roomRepository;

    private final ImageUploadService imageUploadService;

    private final HotelService hotelService;

    private final JwtService jwtService;

    public RoomService(RoomRepository roomRepository, ImageUploadService imageUploadService, HotelService hotelService, JwtService jwtService) {
        this.roomRepository = roomRepository;
        this.imageUploadService = imageUploadService;
        this.hotelService = hotelService;
        this.jwtService = jwtService;
    }

    public void createRoom(String token, RoomInfo request) throws IOException {
        if (token == null || !token.startsWith("Bearer ")) {
            throw new IllegalArgumentException("Invalid token format");
        }

        String jwt = token.substring(7);
        String email = jwtService.getEmailFromToken(jwt);
        Hotel hotel = hotelService.findHotelByEmail(email);
        //List<String> imageUrls = imageUploadService.uploadImages(images);
        var room = Room.builder()
                .roomNumber(request.getRoomNumber())
                .description(request.getDescription())
                .type(request.getType())
                .price(request.getPrice())
                .numOfAdults(request.getNumOfAdults())
                .numOfChildren(request.getNumOfChildren())
                .hotel(hotel)
                .amenities(request.getAmenities())
                .imageUrls(request.getImageUrls())
                .build();
        roomRepository.save(room);
    }

    public List<Room> getAllRooms(String token) {
        Hotel hotel = hotelService.findHotelByEmail(jwtService.getEmailFromToken(token));
        return roomRepository.findAll().stream().filter(room -> room.getHotel().getId().equals(hotel.getId())).toList();
    }

    public Optional<Room> getRoomById(Long roomId) {
        return roomRepository.findById(roomId);
    }

    public Room updateRoom(Long roomId, RoomInfo roomInfo) {
        Room room = roomRepository.findById(roomId)
                .orElseThrow(() -> new RuntimeException("Room not found"));
        room.setDescription(roomInfo.getDescription());
        room.setType(roomInfo.getType());
        room.setPrice(roomInfo.getPrice());
        room.setNumOfAdults(roomInfo.getNumOfAdults());
        room.setNumOfChildren(roomInfo.getNumOfChildren());
        return roomRepository.save(room);


    }

    public void deleteRoom(Long roomId) {
        Room room = roomRepository.findById(roomId)
                .orElseThrow(() -> new RuntimeException("Room not found"));
        roomRepository.delete(room);
    }
}