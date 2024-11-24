package com.brigada.carsh.service;

import com.brigada.carsh.repository.BookingRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BookingService {
    private final BookingRepository bookingRepository;

//    public BookingResponseDTO book(Long carId, Long userId) {
//
//        Booking b = bookingRepository.save();
//        return new BookingResponseDTO()
//    }
}
