package com.demo.hotel_booking.dto.request;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class RoomCreationRequest {
    private String roomNumber;
    private String description;
    private Integer type;
    private Boolean status;
    private BigDecimal price;
    private int numOfAdults;
    private int numOfChildren;
    private String token;
}