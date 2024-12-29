package com.demo.hotel_booking.service;

import com.demo.hotel_booking.dto.request.RoomCreationRequest;
import com.demo.hotel_booking.entity.Room;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

public interface IRoomService {
    void createRoom(String token, RoomCreationRequest room, List<MultipartFile> file) throws IOException;
    List<Room> getAllRooms(String token);
    Optional<Room> getRoomById(Long roomId);
    Room updateRoom(Long roomId, Room roomDetails);
    void deleteRoom(Long roomId);
}