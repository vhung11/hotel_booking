package com.demo.hotel_booking.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Room {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String roomNumber;
    private String description;
    private String type;
    private Boolean available;
    private BigDecimal price;
    private int numOfAdults;
    private int numOfChildren;
    @ElementCollection
    private List<String> amenities;

    @ElementCollection
    private List<String> imageUrls;

    @ManyToOne()
    @JoinColumn(name = "hotel_id")
    private Hotel hotel;

    @OneToMany(mappedBy = "room", cascade = CascadeType.ALL)
    private List<BookedRoom> bookings;

}