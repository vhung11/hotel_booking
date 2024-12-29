package com.demo.hotel_booking.dto.request;

import com.demo.hotel_booking.entity.Coordinates;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class HotelRegistrationRequest {
    private String name;
    private String province;
    private String district;
    private String ward;
    private String email;
    private String password;
    private Double latitude;
    private Double longitude;
    private String type;
}