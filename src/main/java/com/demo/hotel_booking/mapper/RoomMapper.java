package com.demo.hotel_booking.mapper;

import com.demo.hotel_booking.dto.request.RoomInfo;
import com.demo.hotel_booking.entity.Room;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class RoomMapper {
    public RoomInfo toRoomInfo(Room room) {
        return RoomInfo.builder()
                .roomNumber(room.getRoomNumber())
                .description(room.getDescription())
                .price(room.getPrice())
                .type(room.getType())
                .numOfChildren(room.getNumOfChildren())
                .numOfAdults(room.getNumOfAdults())
                .amenities(room.getAmenities())
                .imageUrls(room.getImageUrls())
                .build();
    }

    public List<RoomInfo> toRoomInfoList(List<Room> rooms) {
        return rooms.stream()
                .map(this::toRoomInfo)
                .collect(Collectors.toList());
    }
}
