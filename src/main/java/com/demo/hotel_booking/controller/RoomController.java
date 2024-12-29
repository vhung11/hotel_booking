package com.demo.hotel_booking.controller;

import com.demo.hotel_booking.dto.request.RoomInfo;
import com.demo.hotel_booking.entity.Room;
import com.demo.hotel_booking.security.JwtService;
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
    private final JwtService jwtService;

    @PostMapping("/add-room")
    public ResponseEntity<?> createRoom(
            @RequestHeader("Authorization") String token,
            @RequestBody RoomInfo request,
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
    public ResponseEntity<Room> updateRoom(
            @RequestHeader("Authorization") String token,
            @PathVariable Long roomId,
            @RequestBody RoomInfo roomInfo,
            @RequestParam @Nullable List<MultipartFile> images) {
        String role = jwtService.extractRoleFromToken(token);
        if ("MANAGER".equals(role)) {
            Room updatedRoom = roomService.updateRoom(roomId, roomInfo);
            return ResponseEntity.ok(updatedRoom);
        } else {
            return ResponseEntity.status(403).build(); // Forbidden if not an ADMIN
        }
    }

    @DeleteMapping("/delete/{roomId}")
    public ResponseEntity<Void> deleteRoom(@PathVariable Long roomId) {
        roomService.deleteRoom(roomId);
        return ResponseEntity.noContent().build();
    }
}