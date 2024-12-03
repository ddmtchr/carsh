package com.brigada.carsh.dto.response;

import com.brigada.carsh.domain.supportticket.IssueType;
import com.brigada.carsh.domain.supportticket.TicketStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class SupportTicketResponseDTO {
    private Long id;
    private Long userId;
    private Long bookingId;
    private IssueType issueType;
    private String description;
    private TicketStatus status;
    private LocalDateTime createdAt;
}
