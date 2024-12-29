package com.demo.hotel_booking.dto.request;

import lombok.*;
import lombok.experimental.FieldDefaults;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class RoomCreationRequest {
    private String roomNumber;
    private String description;
    private String type;
    private BigDecimal price;
    private int numOfAdults;
    private int numOfChildren;
    private List<String> amenities;

    //private List<MultipartFile> images;
}