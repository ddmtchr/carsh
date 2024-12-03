package com.brigada.carsh.controller;

import com.brigada.carsh.dto.request.PaymentRequestDTO;
import com.brigada.carsh.dto.response.PaymentResponseDTO;
import com.brigada.carsh.security.service.UserService;
import com.brigada.carsh.service.PaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/payments")
@RequiredArgsConstructor
public class PaymentController {
    private final PaymentService paymentService;
    private final UserService userService;

    @GetMapping("/booking")
    public ResponseEntity<PaymentResponseDTO> getPaymentByBookingId(@RequestParam Long bookingId) {
        PaymentResponseDTO paymentResponseDTO = paymentService.getPaymentByBookingId(bookingId);
        if (paymentResponseDTO == null) return ResponseEntity.noContent().build();
        return ResponseEntity.ok(paymentResponseDTO);
    }

    @GetMapping("/user")
    public ResponseEntity<List<PaymentResponseDTO>> getAllPaymentsByUserId(@RequestParam Long userId) {
        return ResponseEntity.ok(paymentService.getAllPaymentsByUserId(userId));
    }

    @GetMapping("/my")
    public ResponseEntity<List<PaymentResponseDTO>> getUserPayments() {
        return ResponseEntity.ok(paymentService.getAllPaymentsByUserId(userService.getCurrentUser().getId()));
    }

    @PostMapping
    public ResponseEntity<PaymentResponseDTO> createPayment(@RequestParam Long bookingId, @RequestBody PaymentRequestDTO paymentRequestDTO) {
        return new ResponseEntity<>(paymentService.createPayment(paymentRequestDTO, bookingId), HttpStatus.CREATED);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePayment(@PathVariable Long id) {
        paymentService.deletePayment(id);
        return ResponseEntity.noContent().build();
    }

}
