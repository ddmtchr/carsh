package com.brigada.carsh.dto.response;

import com.brigada.carsh.domain.documentverification.DocumentType;
import com.brigada.carsh.domain.documentverification.VerificationStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class DocumentVerificationResponseDTO {
    private Long id;
    private Long userId;
    private String number;
    private DocumentType documentType;
    private VerificationStatus status;
    private LocalDateTime verificationDate;
}
