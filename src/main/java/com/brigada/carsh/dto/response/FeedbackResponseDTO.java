package com.brigada.carsh.dto.response;

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
public class FeedbackResponseDTO {
    private Long id;
    private Long bookingId;
    private BigDecimal rating;
    private String comment;
    private LocalDateTime date;
}
