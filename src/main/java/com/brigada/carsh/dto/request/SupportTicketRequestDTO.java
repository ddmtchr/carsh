package com.brigada.carsh.dto.request;

import com.brigada.carsh.domain.supportticket.IssueType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class SupportTicketRequestDTO {
    private Long bookingId;
    private IssueType issueType;
    private String description;
}
