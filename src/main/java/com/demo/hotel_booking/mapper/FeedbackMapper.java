package com.demo.hotel_booking.mapper;

import com.demo.hotel_booking.dto.request.FeedbackRequest;
import com.demo.hotel_booking.dto.response.FeedbackResponse;
import com.demo.hotel_booking.entity.Feedback;
import com.demo.hotel_booking.entity.Room;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
public class FeedbackMapper {
    public Feedback toFeedback(FeedbackRequest request) {
        return Feedback.builder()
                .note(request.note())
                .comment(request.comment())
                .room(Room.builder()
                        .id(request.roomId())
                        .build()
                )
                .build();
    }

    public FeedbackResponse toFeedbackResponse(Feedback feedback, Integer id) {
        return FeedbackResponse.builder()
                .note(feedback.getNote())
                .comment(feedback.getComment())
                .ownFeedback(Objects.equals(feedback.getCreatedBy(), id))
                .build();
    }
}
