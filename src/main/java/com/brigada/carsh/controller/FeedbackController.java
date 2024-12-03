package com.brigada.carsh.controller;

import com.brigada.carsh.dto.request.FeedbackRequestDTO;
import com.brigada.carsh.dto.response.FeedbackResponseDTO;
import com.brigada.carsh.service.FeedbackService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/feedback")
@RequiredArgsConstructor
public class FeedbackController {
    private final FeedbackService feedbackService;

    @GetMapping("/booking")
    public ResponseEntity<List<FeedbackResponseDTO>> getFeedbackByBooking(@RequestParam Long bookingId) {
        return ResponseEntity.ok(feedbackService.getFeedbackByBookingId(bookingId));
    }

    @GetMapping("/{id}")
    public ResponseEntity<FeedbackResponseDTO> getFeedbackById(@PathVariable Long id) {
        return ResponseEntity.ok(feedbackService.getFeedbackById(id));
    }

    @PostMapping
    public ResponseEntity<FeedbackResponseDTO> createFeedback(@RequestParam Long bookingId, @RequestBody FeedbackRequestDTO feedbackRequestDTO) {
        return new ResponseEntity<>(feedbackService.createFeedback(bookingId, feedbackRequestDTO), HttpStatus.CREATED);
    }
}
