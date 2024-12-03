package com.brigada.carsh.controller;

import com.brigada.carsh.dto.request.FineRequestDTO;
import com.brigada.carsh.dto.response.FineResponseDTO;
import com.brigada.carsh.security.service.UserService;
import com.brigada.carsh.service.FineService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/fines")
@RequiredArgsConstructor
public class FineController {
    private final FineService fineService;
    private final UserService userService;

    @GetMapping("/user")
    public ResponseEntity<List<FineResponseDTO>> getAllFinesByUser(@RequestParam Long userId) {
        return ResponseEntity.ok(fineService.getAllFinesByUser(userId));
    }

    @GetMapping("/my")
    public ResponseEntity<List<FineResponseDTO>> getAllUsersFines() {
        return ResponseEntity.ok(fineService.getAllFinesByUser(userService.getCurrentUser().getId()));
    }

    @GetMapping("/ticket")
    public ResponseEntity<FineResponseDTO> getFineBySupportTicket(@RequestParam Long ticketId) {
        FineResponseDTO fineResponseDTO = fineService.getFineBySupportTicket(ticketId);
        if (fineResponseDTO == null) return ResponseEntity.noContent().build();
        return ResponseEntity.ok(fineResponseDTO);
    }

    @PostMapping
    public ResponseEntity<FineResponseDTO> createFine(@RequestParam Long userId, @RequestParam(required = false) Long ticketId, @RequestBody FineRequestDTO fineRequestDTO) {
        return new ResponseEntity<>(fineService.createFine(userId, ticketId, fineRequestDTO), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<FineResponseDTO> setPaid(@PathVariable Long id) {
        return ResponseEntity.ok(fineService.setPaid(id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteFine(@PathVariable Long id) {
        fineService.deleteFine(id);
        return ResponseEntity.noContent().build();
    }
}
