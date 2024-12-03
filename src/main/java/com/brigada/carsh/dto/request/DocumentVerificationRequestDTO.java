package com.brigada.carsh.dto.request;

import com.brigada.carsh.domain.documentverification.DocumentType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class DocumentVerificationRequestDTO {
    private DocumentType documentType;
}
