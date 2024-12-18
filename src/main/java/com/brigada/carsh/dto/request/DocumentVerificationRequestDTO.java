package com.brigada.carsh.dto.request;

import com.brigada.carsh.domain.documentverification.DocumentType;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class DocumentVerificationRequestDTO {
    @Size(min = 10, max = 10)
    private String number;
    private DocumentType documentType;
}
