package com.brigada.carsh.domain.feedback;

import com.brigada.carsh.domain.booking.Booking;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "feedback")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Feedback {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "booking_id")
    private Booking booking;

    @Column(name = "rating", nullable = true)
    private BigDecimal rating;

    @Column(name = "comment", nullable = true)
    private String comment;

    @Column(name = "date", nullable = false)
    private LocalDateTime date;
}
