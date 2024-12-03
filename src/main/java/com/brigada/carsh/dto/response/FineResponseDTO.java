package com.brigada.carsh.dto.response;

import com.brigada.carsh.domain.fine.FineStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class FineResponseDTO {
    private Long id;
    private Long userId;
    private Long supportTicketId;
    private BigDecimal amount;
    private LocalDateTime issuedDate;
    private FineStatus status;
    private String reason;
}
