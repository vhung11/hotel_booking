package com.demo.hotel_booking.service;

import com.demo.hotel_booking.dto.request.RoomCreationRequest;
import com.demo.hotel_booking.entity.Hotel;
import com.demo.hotel_booking.entity.Room;
import com.demo.hotel_booking.repository.RoomRepository;
import com.demo.hotel_booking.security.JwtService;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class RoomServiceImpl implements RoomService {
    private final RoomRepository roomRepository;

    private final ImageUploadService imageUploadService;

    private final HotelService hotelService;

    private final JwtService jwtService;

    public RoomServiceImpl(RoomRepository roomRepository, ImageUploadService imageUploadService, HotelService hotelService, JwtService jwtService) {
        this.roomRepository = roomRepository;
        this.imageUploadService = imageUploadService;
        this.hotelService = hotelService;
        this.jwtService = jwtService;
    }

    @Override
    public Room createRoom(RoomCreationRequest roomRequest, MultipartFile file) throws IOException {
        Hotel hotel = hotelService.findHotelByEmail(jwtService.getEmailFromToken(roomRequest.getToken()));
        Room room = Room.builder()
                .roomNumber(roomRequest.getRoomNumber())
                .description(roomRequest.getDescription())
                .type(roomRequest.getType())
                .status(roomRequest.getStatus())
                .price(roomRequest.getPrice())
                .numOfAdults(roomRequest.getNumOfAdults())
                .numOfChildren(roomRequest.getNumOfChildren())
                .hotel(hotel)
                .build();

        if (file != null && !file.isEmpty()) {
            Map uploadResult = imageUploadService.uploadImage(file);
            String imageUrl = (String) uploadResult.get("url");
            room.getImages().add(imageUrl);
        }
        return roomRepository.save(room);
    }

    @Override
    public List<Room> getAllRooms(String token) {
        Hotel hotel = hotelService.findHotelByEmail(jwtService.getEmailFromToken(token));
        return roomRepository.findAll().stream().filter(room -> room.getHotel().getId().equals(hotel.getId())).toList();
    }

    @Override
    public Optional<Room> getRoomById(Long roomId) {
        return roomRepository.findById(roomId);
    }

    @Override
    public Room updateRoom(Long roomId, Room roomDetails) {
        Room room = roomRepository.findById(roomId)
                .orElseThrow(() -> new RuntimeException("Room not found"));
        room.setDescription(roomDetails.getDescription());
        room.setType(roomDetails.getType());
        room.setStatus(roomDetails.getStatus());
        room.setPrice(roomDetails.getPrice());
        room.setNumOfAdults(roomDetails.getNumOfAdults());
        room.setNumOfChildren(roomDetails.getNumOfChildren());
        room.setImages(roomDetails.getImages());
        room.setHotel(roomDetails.getHotel());
        return roomRepository.save(room);
    }

    @Override
    public void deleteRoom(Long roomId) {
        Room room = roomRepository.findById(roomId)
                .orElseThrow(() -> new RuntimeException("Room not found"));
        roomRepository.delete(room);
    }
}