package com.demo.hotel_booking.controller;

import com.demo.hotel_booking.dto.request.RoomCreationRequest;
import com.demo.hotel_booking.entity.Room;
import com.demo.hotel_booking.service.IRoomService;
import com.demo.hotel_booking.service.RoomServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/manager/rooms")
public class RoomController {
    private final IRoomService roomService;

    @PostMapping("/add-room")
    public ResponseEntity<?> createRoom(
            @RequestHeader("Authorization") String token,
            @RequestParam String roomNumber,
            @RequestParam String description,
            @RequestParam String type,
            @RequestParam BigDecimal price,
            @RequestParam int numOfAdults,
            @RequestParam int numOfChildren,
            @RequestParam List<String> amenities,
            @RequestParam @Nullable List<MultipartFile> images) throws IOException {
        RoomCreationRequest request = new RoomCreationRequest();
        request.setRoomNumber(roomNumber);
        request.setDescription(description);
        request.setType(type);
        request.setPrice(price);
        request.setNumOfAdults(numOfAdults);
        request.setNumOfChildren(numOfChildren);
        request.setAmenities(amenities);
        roomService.createRoom(token, request, images);
        return ResponseEntity.ok("Room created successfully");
    }

    @GetMapping("/all-rooms")
    public ResponseEntity<List<Room>> getAllRooms(@RequestParam String token) {
        List<Room> rooms = roomService.getAllRooms(token);
        return ResponseEntity.ok(rooms);
    }

    @GetMapping("/rooms/{roomId}")
    public ResponseEntity<Room> getRoomById(@PathVariable Long roomId) {
        Room room = roomService.getRoomById(roomId).orElseThrow(() -> new RuntimeException("Room not found"));
        return ResponseEntity.ok(room);
    }

    @PutMapping("/update/{roomId}")
    public ResponseEntity<Room> updateRoom(@PathVariable Long roomId, @RequestBody Room roomDetails) {
        Room updatedRoom = roomService.updateRoom(roomId, roomDetails);
        return ResponseEntity.ok(updatedRoom);
    }

    @DeleteMapping("/delete/{roomId}")
    public ResponseEntity<Void> deleteRoom(@PathVariable Long roomId) {
        roomService.deleteRoom(roomId);
        return ResponseEntity.noContent().build();
    }
}