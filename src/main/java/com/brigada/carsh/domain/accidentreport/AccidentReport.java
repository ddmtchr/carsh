package com.brigada.carsh.domain.accidentreport;

import com.brigada.carsh.domain.supportticket.SupportTicket;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "accident_report")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class AccidentReport {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "ticket_id")
    private SupportTicket supportTicket;

    @Column(name = "report_date", nullable = false)
    private LocalDateTime reportDate;

    @Column(name = "report_details", nullable = true)
    private String reportDetails;

    @Column(name = "is_guilty", nullable = false)
    private Boolean isGuilty;
}
