package com.brigada.carsh.service;

import com.brigada.carsh.domain.booking.Booking;
import com.brigada.carsh.domain.feedback.Feedback;
import com.brigada.carsh.dto.request.FeedbackRequestDTO;
import com.brigada.carsh.dto.response.FeedbackResponseDTO;
import com.brigada.carsh.exception.BookingNotFoundException;
import com.brigada.carsh.exception.FeedbackNotFoundException;
import com.brigada.carsh.mapper.FeedbackMapper;
import com.brigada.carsh.repository.BookingRepository;
import com.brigada.carsh.repository.FeedbackRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class FeedbackService {
    private final FeedbackRepository feedbackRepository;
    private final BookingRepository bookingRepository;

    public List<FeedbackResponseDTO> getFeedbackByBookingId(Long bookingId) {
        if (!bookingRepository.existsById(bookingId)) {
            throw new BookingNotFoundException(String.format("Booking with id=%s was not found", bookingId));
        }
        return feedbackRepository.findAllByBooking_Id(bookingId).stream().map(FeedbackMapper.INSTANCE::toResponseDTO).toList();
    }

    public FeedbackResponseDTO getFeedbackById(Long id) {
        Feedback feedback = feedbackRepository.findById(id).orElseThrow(() -> new FeedbackNotFoundException(String.format("Feedback with id=%s was not found", id)));
        return FeedbackMapper.INSTANCE.toResponseDTO(feedback);
    }

    @Transactional
    public FeedbackResponseDTO createFeedback(Long bookingId, FeedbackRequestDTO feedbackRequestDTO) {
        Booking booking = bookingRepository.findById(bookingId).orElseThrow(() -> new BookingNotFoundException(String.format("Booking with id=%s was not found", bookingId)));
        Feedback feedback = FeedbackMapper.INSTANCE.toEntity(feedbackRequestDTO);
        feedback.setBooking(booking);
        feedback.setDate(LocalDateTime.now());
        return FeedbackMapper.INSTANCE.toResponseDTO(feedbackRepository.save(feedback));
    }
}
