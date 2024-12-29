package com.demo.hotel_booking.controller;

import com.demo.hotel_booking.dto.request.RoomCreationRequest;
import com.demo.hotel_booking.entity.Room;
import com.demo.hotel_booking.service.RoomService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/manager/rooms")
public class RoomController {
    private final RoomService roomService;

    @PostMapping("/add-room")
    public ResponseEntity<?> createRoom(
            @RequestHeader("Authorization") String token,
            @RequestBody RoomCreationRequest request,
            @RequestParam @Nullable List<MultipartFile> images) throws IOException {
        roomService.createRoom(token, request, images);
        return ResponseEntity.ok("Room created successfully");
    }

    @GetMapping("/all-rooms")
    public ResponseEntity<List<Room>> getAllRooms(@RequestHeader("Authorization") String token) {
        return ResponseEntity.ok(roomService.getAllRooms(token));
    }

    @GetMapping("/rooms/{roomId}")
    public ResponseEntity<Room> getRoomById(@PathVariable Long roomId) {
        Room room = roomService.getRoomById(roomId).orElseThrow(() -> new RuntimeException("Room not found"));
        return ResponseEntity.ok(room);
    }

    @PutMapping("/update/{roomId}")
    public ResponseEntity<Room> updateRoom(@RequestParam Long roomId, @RequestBody Room roomDetails) {
        Room updatedRoom = roomService.updateRoom(roomId, roomDetails);
        return ResponseEntity.ok(updatedRoom);
    }

    @DeleteMapping("/delete/{roomId}")
    public ResponseEntity<Void> deleteRoom(@PathVariable Long roomId) {
        roomService.deleteRoom(roomId);
        return ResponseEntity.noContent().build();
    }
}