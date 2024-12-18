package com.brigada.carsh.controller;

import com.brigada.carsh.domain.documentverification.VerificationStatus;
import com.brigada.carsh.dto.request.DocumentVerificationRequestDTO;
import com.brigada.carsh.dto.response.DocumentVerificationResponseDTO;
import com.brigada.carsh.security.service.UserService;
import com.brigada.carsh.service.DocumentVerificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/document-verification")
@RequiredArgsConstructor
public class DocumentVerificationController {
    private final DocumentVerificationService documentVerificationService;
    private final UserService userService;

    @GetMapping("/user")
    public ResponseEntity<List<DocumentVerificationResponseDTO>> getAllVerificationsByUserId(@RequestParam Long userId) {
        return ResponseEntity.ok(documentVerificationService.getAllVerificationsByUser(userId));
    }

    @GetMapping("/my")
    public ResponseEntity<List<DocumentVerificationResponseDTO>> getAllUserVerifications() {
        return ResponseEntity.ok(documentVerificationService.getAllVerificationsByUser(userService.getCurrentUser().getId()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<DocumentVerificationResponseDTO> getVerificationById(@PathVariable Long id) {
        return ResponseEntity.ok(documentVerificationService.getVerificationById(id));
    }

    @GetMapping("/pending")
    public ResponseEntity<List<DocumentVerificationResponseDTO>> getAllPending() {
        return ResponseEntity.ok(documentVerificationService.getAllVerificationsByStatus(VerificationStatus.PENDING));
    }

    @PostMapping
    public ResponseEntity<DocumentVerificationResponseDTO> createVerification(@RequestBody DocumentVerificationRequestDTO documentVerificationRequestDTO) {
        return new ResponseEntity<>(documentVerificationService.createVerification(documentVerificationRequestDTO, userService.getCurrentUser().getId()), HttpStatus.CREATED);
    }

    @PutMapping("/{id}/verify")
    public ResponseEntity<DocumentVerificationResponseDTO> verifyDocument(@PathVariable Long id) {
        return ResponseEntity.ok(documentVerificationService.verifyDocument(id));
    }

    @PutMapping("/{id}/reject")
    public ResponseEntity<DocumentVerificationResponseDTO> rejectDocument(@PathVariable Long id) {
        return ResponseEntity.ok(documentVerificationService.rejectDocument(id));
    }
}
