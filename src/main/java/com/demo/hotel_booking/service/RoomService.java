package com.demo.hotel_booking.service;

import com.demo.hotel_booking.dto.request.RoomInfo;
import com.demo.hotel_booking.entity.Hotel;
import com.demo.hotel_booking.entity.Room;
import com.demo.hotel_booking.mapper.RoomMapper;
import com.demo.hotel_booking.repository.HotelRepository;
import com.demo.hotel_booking.repository.RoomRepository;
import com.demo.hotel_booking.security.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Service
public class RoomService {
    @Autowired
    private  RoomRepository roomRepository;
    @Autowired
    private  HotelRepository hotelRepository;
    @Autowired
    private RoomMapper roomMapper;
    @Autowired
    private  JwtService jwtService;


    public void createRoom(String token, RoomInfo request) {
        if (token == null || !token.startsWith("Bearer ")) {
            throw new IllegalArgumentException("Invalid token format");
        }

        String jwt = token.substring(7);
        String email = jwtService.getEmailFromToken(jwt);
        Hotel hotel = hotelRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Hotel not found"));
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
                .available(true)
                .build();
        roomRepository.save(room);
    }

    public List<RoomInfo> getAllRooms(String token) {
        if (token == null || !token.startsWith("Bearer ")) {
            throw new IllegalArgumentException("Invalid token format");
        }

        String jwt = token.substring(7);
        String email = jwtService.getEmailFromToken(jwt);
        Hotel hotel = hotelRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Hotel not found"));
        //return roomRepository.findAll().stream().filter(room -> room.getHotel().getId().equals(hotel.getId())).toList();
        List<Room> rooms = roomRepository.findByHotelId(hotel.getId());
        return roomMapper.toRoomInfoList(rooms);
    }

    public Optional<Room> getRoomById(Long roomId) {
        return roomRepository.findById(roomId);
    }

    public void updateRoom(Long roomId, RoomInfo roomInfo) {
        Room room = roomRepository.findById(roomId)
                .orElseThrow(() -> new RuntimeException("Room not found"));
        room.setRoomNumber(roomInfo.getRoomNumber());
        room.setDescription(roomInfo.getDescription());
        room.setType(roomInfo.getType());
        room.setPrice(roomInfo.getPrice());
        room.setNumOfAdults(roomInfo.getNumOfAdults());
        room.setNumOfChildren(roomInfo.getNumOfChildren());
        room.setAmenities(roomInfo.getAmenities());
        room.setImageUrls(roomInfo.getImageUrls());
        roomRepository.save(room);
    }

    public void deleteRoom(String token, Long roomId) {
        Room room = roomRepository.findById(roomId)
                .orElseThrow(() -> new RuntimeException("Room not found"));
        roomRepository.delete(room);
    }
}