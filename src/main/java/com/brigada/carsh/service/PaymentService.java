package com.brigada.carsh.service;

import com.brigada.carsh.domain.booking.Booking;
import com.brigada.carsh.domain.payment.Payment;
import com.brigada.carsh.dto.request.PaymentRequestDTO;
import com.brigada.carsh.dto.response.PaymentResponseDTO;
import com.brigada.carsh.exception.BookingNotFoundException;
import com.brigada.carsh.exception.InconsistentRequestException;
import com.brigada.carsh.exception.UserNotFoundException;
import com.brigada.carsh.mapper.PaymentMapper;
import com.brigada.carsh.repository.BookingRepository;
import com.brigada.carsh.repository.PaymentRepository;
import com.brigada.carsh.security.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PaymentService {
    private final PaymentRepository paymentRepository;
    private final BookingRepository bookingRepository;
    private final UserRepository userRepository;

    public PaymentResponseDTO getPaymentByBookingId(Long bookingId) {
        if (!bookingRepository.existsById(bookingId)) throw new BookingNotFoundException(String.format("Booking with id=%s was not found", bookingId));
        Optional<Payment> optional = paymentRepository.findByBooking_Id(bookingId);
        return optional.map(PaymentMapper.INSTANCE::toResponseDTO).orElse(null);
    }

    public List<PaymentResponseDTO> getAllPaymentsByUserId(Long userId) {
        if (!userRepository.existsById(userId)) throw new UserNotFoundException(String.format("User with id=%s was not found", userId));
        return paymentRepository.findAllByBooking_User_Id(userId).stream().map(PaymentMapper.INSTANCE::toResponseDTO).toList();
    }

    public PaymentResponseDTO createPayment(PaymentRequestDTO paymentRequestDTO, Long bookingId) {
        Booking booking = bookingRepository.findById(bookingId).orElseThrow(() -> new BookingNotFoundException(String.format("Booking with id=%s was not found", bookingId)));
        if (paymentRepository.findByBooking_Id(bookingId).isPresent()) throw new InconsistentRequestException(String.format("Payment for booking id=%s already exists", bookingId));
        Payment payment = PaymentMapper.INSTANCE.toEntity(paymentRequestDTO);
        payment.setBooking(booking);
        payment.setPaymentDate(LocalDateTime.now());
        return PaymentMapper.INSTANCE.toResponseDTO(paymentRepository.save(payment));
    }

    public void deletePayment(Long paymentId) {
        paymentRepository.deleteById(paymentId);
    }
}
