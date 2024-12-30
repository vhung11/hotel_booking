package com.demo.hotel_booking.controller;

import com.demo.hotel_booking.dto.request.RoomInfo;
import com.demo.hotel_booking.entity.Room;
import com.demo.hotel_booking.security.JwtService;
import com.demo.hotel_booking.service.RoomService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/manager/rooms")
public class RoomController {
    @Autowired
    private RoomService roomService;
    @Autowired
    private JwtService jwtService;

    @PostMapping("/add-room")
    public ResponseEntity<?> createRoom(@RequestHeader("Authorization") String token, @RequestBody RoomInfo request) {
        String role = jwtService.extractRoleFromToken(token.substring(7));
        if ("MANAGER".equals(role)) {
            roomService.createRoom(token, request);
            return ResponseEntity.ok("Room created successfully");
        } else {
            return ResponseEntity.status(403).build(); // Forbidden if not an ADMIN
        }

    }

    @GetMapping("/all-rooms")
    public ResponseEntity<List<RoomInfo>> getAllRooms(@RequestHeader("Authorization") String token) {
        return ResponseEntity.ok(roomService.getAllRooms(token));
    }

    @GetMapping("/get-room/{roomId}")
    public ResponseEntity<Room> getRoomById(@PathVariable Long roomId) {
        Room room = roomService.getRoomById(roomId).orElseThrow(() -> new RuntimeException("Room not found"));
        return ResponseEntity.ok(room);
    }

    @PutMapping("/update/{roomId}")
    public ResponseEntity<?> updateRoom(
            @RequestHeader("Authorization") String token,
            @PathVariable Long roomId,
            @RequestBody RoomInfo roomInfo) {
        String role = jwtService.extractRoleFromToken(token.substring(7));
        if ("MANAGER".equals(role)) {
            roomService.updateRoom(roomId, roomInfo);
            return ResponseEntity.ok("Room updated successfully");
        } else {
            return ResponseEntity.status(403).build(); // Forbidden if not an ADMIN
        }
    }

    @DeleteMapping("/delete/{roomId}")
    public ResponseEntity<?> deleteRoom(@RequestHeader("Authorization") String token,
                                        @PathVariable Long roomId) {
        String role = jwtService.extractRoleFromToken(token.substring(7));
        if ("MANAGER".equals(role)) {
            roomService.deleteRoom(token, roomId);
            return ResponseEntity.ok("Rom deleted successfully");
        } else {
            return ResponseEntity.status(403).build(); // Forbidden if not an ADMIN
        }
    }
}