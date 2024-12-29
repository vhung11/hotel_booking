package com.demo.hotel_booking.service;

import com.demo.hotel_booking.common.PageResponse;
import com.demo.hotel_booking.dto.request.FeedbackRequest;
import com.demo.hotel_booking.dto.response.FeedbackResponse;
import com.demo.hotel_booking.entity.Feedback;
import com.demo.hotel_booking.entity.Room;
import com.demo.hotel_booking.entity.User;
import com.demo.hotel_booking.exception.OperationNotPermittedException;
import com.demo.hotel_booking.mapper.FeedbackMapper;
import com.demo.hotel_booking.repository.FeedBackRepository;
import com.demo.hotel_booking.repository.RoomRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.awt.print.Book;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class FeedbackService {

    private final FeedBackRepository feedBackRepository;
    private final RoomRepository roomRepository;
    private final FeedbackMapper feedbackMapper;

    public Integer save(FeedbackRequest request, Authentication connectedUser) {
        Room room = roomRepository.findById(request.roomId())
                .orElseThrow(() -> new EntityNotFoundException("No room found with ID:: " + request.roomId()));
        // User user = ((User) connectedUser.getPrincipal());
        Feedback feedback = feedbackMapper.toFeedback(request);
        return feedBackRepository.save(feedback).getId();
    }

    @Transactional
    public PageResponse<FeedbackResponse> findAllFeedbacksByRoom(Long roomId, int page, int size, Authentication connectedUser) {
        Pageable pageable = PageRequest.of(page, size);
        User user = ((User) connectedUser.getPrincipal());
        Page<Feedback> feedbacks = feedBackRepository.findAllByRoomId(roomId, pageable);
        List<FeedbackResponse> feedbackResponses = feedbacks.stream()
                .map(f -> feedbackMapper.toFeedbackResponse(f, user.getId()))
                .toList();
        return new PageResponse<>(
                feedbackResponses,
                feedbacks.getNumber(),
                feedbacks.getSize(),
                feedbacks.getTotalElements(),
                feedbacks.getTotalPages(),
                feedbacks.isFirst(),
                feedbacks.isLast()
        );

    }
}
