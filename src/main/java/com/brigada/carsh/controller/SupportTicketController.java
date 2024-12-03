package com.brigada.carsh.controller;

import com.brigada.carsh.domain.supportticket.TicketStatus;
import com.brigada.carsh.dto.request.SupportTicketRequestDTO;
import com.brigada.carsh.dto.response.SupportTicketResponseDTO;
import com.brigada.carsh.security.service.UserService;
import com.brigada.carsh.service.SupportTicketService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/support-tickets")
@RequiredArgsConstructor
public class SupportTicketController {
    private final SupportTicketService supportTicketService;
    private final UserService userService;

    @GetMapping
    public ResponseEntity<List<SupportTicketResponseDTO>> getAllSupportTickets() {
        return ResponseEntity.ok(supportTicketService.getAllSupportTickets());
    }

    @GetMapping("/status")
    public ResponseEntity<List<SupportTicketResponseDTO>> getAllSupportTicketsByStatus(@RequestParam TicketStatus status) {
        return ResponseEntity.ok(supportTicketService.getAllSupportTicketsByStatus(status));
    }

    @GetMapping("/my")
    public ResponseEntity<List<SupportTicketResponseDTO>> getAllSupportTicketsByUser() {
        Long userId = userService.getCurrentUser().getId();
        return ResponseEntity.ok(supportTicketService.getAllSupportTicketsByUser(userId));
    }

    @GetMapping("/{id}")
    public ResponseEntity<SupportTicketResponseDTO> getSupportTicketById(@PathVariable Long id) {
        return ResponseEntity.ok(supportTicketService.getSupportTicketById(id));
    }

    @PostMapping
    public ResponseEntity<SupportTicketResponseDTO> createSupportTicket(@RequestBody SupportTicketRequestDTO supportTicketRequestDTO) {
        Long userId = userService.getCurrentUser().getId();
        return new ResponseEntity<>(supportTicketService.createSupportTicket(supportTicketRequestDTO, userId), HttpStatus.CREATED);
    }

    @PutMapping("/{id}/accept")
    public ResponseEntity<SupportTicketResponseDTO> acceptSupportTicket(@PathVariable Long id) {
        return ResponseEntity.ok(supportTicketService.acceptSupportTicket(id));
    }

    @PutMapping("/{id}/close")
    public ResponseEntity<SupportTicketResponseDTO> closeSupportTicket(@PathVariable Long id) {
        return ResponseEntity.ok(supportTicketService.closeSupportTicket(id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSupportTicket(@PathVariable Long id) {
        supportTicketService.deleteSupportTicket(id);
        return ResponseEntity.noContent().build();
    }
}
