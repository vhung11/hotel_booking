package com.demo.hotel_booking.controller;

import com.demo.hotel_booking.dto.request.RoomCreationRequest;
import com.demo.hotel_booking.entity.Room;
import com.demo.hotel_booking.service.RoomServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/managers")
public class RoomController {
    private final RoomServiceImpl roomService;

    public RoomController(RoomServiceImpl roomService) {
        this.roomService = roomService;
    }

    @PostMapping("/add-rooms")
    public ResponseEntity<Room> createRoom(@RequestPart RoomCreationRequest room, @RequestPart("file") MultipartFile file) {
        try {
            Room createdRoom = roomService.createRoom(room, file);
            return ResponseEntity.ok(createdRoom);
        } catch (IOException e) {
            log.error("e: ", e);
            return ResponseEntity.status(500).body(null);
        }
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