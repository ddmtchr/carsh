package com.brigada.carsh.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class AccidentReportResponseDTO {
    private Long id;
    private Long supportTicketId;
    private LocalDateTime reportDate;
    private String reportDetails;
    private Boolean isGuilty;
}
