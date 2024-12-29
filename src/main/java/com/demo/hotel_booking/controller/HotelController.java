package com.demo.hotel_booking.controller;

import com.demo.hotel_booking.dto.request.HotelRegistrationRequest;
import com.demo.hotel_booking.entity.Hotel;
import com.demo.hotel_booking.entity.Room;
import com.demo.hotel_booking.service.HotelService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/manager/hotels")
public class HotelController {

    private final HotelService hotelService;

    public HotelController(HotelService hotelService) {
        this.hotelService = hotelService;
    }

//    @GetMapping("/available-hotels")
//    public ResponseEntity<List<Hotel>> searchHotels(@RequestParam String address,
//                                                    @RequestParam LocalDate checkInDate,
//                                                    @RequestParam LocalDate checkOutDate,
//                                                    @RequestParam int numOfAdults,
//                                                    @RequestParam int numOfChildren) {
//        List<Hotel> availableHotels = hotelService.findHotelsWithAvailableRooms(address, checkInDate, checkOutDate, numOfAdults, numOfChildren);
//        return ResponseEntity.ok(availableHotels);
//    }

    @GetMapping("/{hotelId}/available-rooms")
    public ResponseEntity<List<Room>> getAvailableRooms(
            @PathVariable Long hotelId,
            @RequestParam LocalDate checkInDate,
            @RequestParam LocalDate checkOutDate,
            @RequestParam int numOfAdults,
            @RequestParam int numOfChildren) {

        List<Room> availableRooms = hotelService.findAvailableRooms(hotelId, checkInDate, checkOutDate, numOfAdults, numOfChildren);

        return ResponseEntity.ok(availableRooms);
    }

    @GetMapping("/{hotelId}/rooms")
    public ResponseEntity<List<Room>> getRoomsByHotelId(@PathVariable Long hotelId) {
        List<Room> rooms = hotelService.findRoomsByHotelId(hotelId);
        return ResponseEntity.ok(rooms);
    }

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<String> hotelRegister(@RequestBody HotelRegistrationRequest request) {
        hotelService.hotelRegister(request);
        return ResponseEntity.status(HttpStatus.CREATED).body("Manager registered successfully");
    }

}