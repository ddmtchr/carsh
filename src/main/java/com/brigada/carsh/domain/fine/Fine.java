package com.brigada.carsh.domain.fine;

import com.brigada.carsh.domain.supportticket.SupportTicket;
import com.brigada.carsh.security.entity.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "fine")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Fine {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "support_ticket_id")
    private SupportTicket supportTicket;

    @Column(name = "amount", nullable = false)
    private BigDecimal amount;

    @Column(name = "issued_date", nullable = false)
    private LocalDateTime issuedDate;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private FineStatus status;

    @Column(name = "reason", nullable = false)
    private String reason;
}
