package com.demo.hotel_booking.dto.request;

import com.demo.hotel_booking.entity.Coordinates;
import lombok.Data;

@Data
public class HotelRegistrationRequest {
    private String email;
    private String password;
    private String hotelName;
    private String hotelAddress;
    private Coordinates coordinates;
}