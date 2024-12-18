package com.brigada.carsh.service;

import com.brigada.carsh.domain.documentverification.DocumentVerification;
import com.brigada.carsh.domain.documentverification.VerificationStatus;
import com.brigada.carsh.dto.request.DocumentVerificationRequestDTO;
import com.brigada.carsh.dto.response.DocumentVerificationResponseDTO;
import com.brigada.carsh.exception.DocumentVerificationNotFoundException;
import com.brigada.carsh.exception.InconsistentRequestException;
import com.brigada.carsh.exception.UserNotFoundException;
import com.brigada.carsh.mapper.DocumentVerificationMapper;
import com.brigada.carsh.repository.DocumentVerificationRepository;
import com.brigada.carsh.security.entity.User;
import com.brigada.carsh.security.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class DocumentVerificationService {
    private final DocumentVerificationRepository documentVerificationRepository;
    private final UserRepository userRepository;

    public List<DocumentVerificationResponseDTO> getAllVerificationsByUser(Long userId) {
        if (!userRepository.existsById(userId)) throw new UserNotFoundException(String.format("User with id=%s was not found", userId));
        return documentVerificationRepository.findAllByUser_Id(userId).stream().map(DocumentVerificationMapper.INSTANCE::toResponseDTO).toList();
    }

    public DocumentVerificationResponseDTO getVerificationById(Long id) {
        DocumentVerification documentVerification = documentVerificationRepository.findById(id).orElseThrow(() -> new DocumentVerificationNotFoundException(String.format("Document verification with id=%s was not found", id)));
        return DocumentVerificationMapper.INSTANCE.toResponseDTO(documentVerification);
    }

    public List<DocumentVerificationResponseDTO> getAllVerificationsByStatus(VerificationStatus status) {
        return documentVerificationRepository.findAllByStatus(status).stream().map(DocumentVerificationMapper.INSTANCE::toResponseDTO).toList();
    }

    @Transactional
    public DocumentVerificationResponseDTO createVerification(DocumentVerificationRequestDTO documentVerificationRequestDTO, Long userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new UserNotFoundException(String.format("User with id=%s was not found", userId)));
        DocumentVerification documentVerification = DocumentVerificationMapper.INSTANCE.toEntity(documentVerificationRequestDTO);
        documentVerification.setUser(user);
        documentVerification.setStatus(VerificationStatus.PENDING);
        return DocumentVerificationMapper.INSTANCE.toResponseDTO(documentVerificationRepository.save(documentVerification));
    }

    @Transactional
    public DocumentVerificationResponseDTO verifyDocument(Long id) {
        DocumentVerification documentVerification = documentVerificationRepository.findById(id).orElseThrow(() -> new DocumentVerificationNotFoundException(String.format("Document verification with id=%s was not found", id)));
        if (documentVerification.getStatus() != VerificationStatus.PENDING) {
            throw new InconsistentRequestException(String.format("Document verification with id=%s cannot be verified", id));
        }
        documentVerification.setStatus(VerificationStatus.VERIFIED);
        documentVerification.setVerificationDate(LocalDateTime.now());
        return DocumentVerificationMapper.INSTANCE.toResponseDTO(documentVerificationRepository.save(documentVerification));
    }

    @Transactional
    public DocumentVerificationResponseDTO rejectDocument(Long id) {
        DocumentVerification documentVerification = documentVerificationRepository.findById(id).orElseThrow(() -> new DocumentVerificationNotFoundException(String.format("Document verification with id=%s was not found", id)));
        if (documentVerification.getStatus() != VerificationStatus.PENDING) {
            throw new InconsistentRequestException(String.format("Document verification with id=%s cannot be rejected", id));
        }
        documentVerification.setStatus(VerificationStatus.REJECTED);
        return DocumentVerificationMapper.INSTANCE.toResponseDTO(documentVerificationRepository.save(documentVerification));
    }

}
