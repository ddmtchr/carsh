package com.brigada.carsh.controller;

import com.brigada.carsh.domain.booking.BookingStatus;
import com.brigada.carsh.dto.request.BookingStartDTO;
import com.brigada.carsh.dto.response.BookingResponseDTO;
import com.brigada.carsh.security.service.UserService;
import com.brigada.carsh.service.BookingService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/bookings")
@RequiredArgsConstructor
public class BookingController {
    private final BookingService bookingService;
    private final UserService userService;

    @GetMapping
    public ResponseEntity<List<BookingResponseDTO>> getAllBookingsByUser(@RequestParam Long userId) {
        return ResponseEntity.ok(bookingService.getAllBookingsByUser(userId));
    }

    @GetMapping("/{id}")
    public ResponseEntity<BookingResponseDTO> getBookingById(@PathVariable Long id) {
        return ResponseEntity.ok(bookingService.getBookingById(id));
    }

    @GetMapping("/status")
    public ResponseEntity<List<BookingResponseDTO>> getAllBookingsByStatus(@RequestParam BookingStatus status) {
        return ResponseEntity.ok(bookingService.getAllBookingsByStatus(status));
    }

    @GetMapping("/my/status")
    public ResponseEntity<List<BookingResponseDTO>> getAllBookingsByUserAndStatus(@RequestParam BookingStatus status) {
        Long userId = userService.getCurrentUser().getId();
        return ResponseEntity.ok(bookingService.getAllBookingsByUserAndStatus(userId, status));
    }

    @PostMapping
    public ResponseEntity<BookingResponseDTO> bookCar(@RequestParam Long carId, @RequestBody BookingStartDTO dto) {
        Long userId = userService.getCurrentUser().getId();
        return new ResponseEntity<>(bookingService.bookCar(dto, carId, userId), HttpStatus.CREATED);
    }

    @PutMapping("/start")
    public ResponseEntity<BookingResponseDTO> startBooking(@RequestParam Long bookingId) {
        Long userId = userService.getCurrentUser().getId();
        return ResponseEntity.ok(bookingService.startBooking(bookingId, userId));
    }

    @PutMapping("/cancel")
    public ResponseEntity<BookingResponseDTO> cancelBooking(@RequestParam Long bookingId) {
        Long userId = userService.getCurrentUser().getId();
        return ResponseEntity.ok(bookingService.cancelBooking(bookingId, userId));
    }

    @Operation(description = "ACHTUNG после этого надо get by id сделать, чтобы оставшиеся поля подтянулись после завершения (они триггерами ставятся)")
    @PutMapping("/end")
    public ResponseEntity<BookingResponseDTO> endBooking(@RequestParam Long bookingId) {
        Long userId = userService.getCurrentUser().getId();
        return ResponseEntity.ok(bookingService.endBooking(bookingId, userId));
    }
}
