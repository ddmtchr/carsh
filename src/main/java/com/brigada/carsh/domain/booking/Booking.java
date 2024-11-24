package com.brigada.carsh.domain.booking;

import com.brigada.carsh.domain.car.Car;
import com.brigada.carsh.domain.insurance.Insurance;
import com.brigada.carsh.domain.location.Location;
import com.brigada.carsh.security.entity.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "booking")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Booking {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "car_id")
    private Car car;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @Column(name = "start_time", nullable = true)
    private LocalDateTime startTime;

    @Column(name = "end_time", nullable = true)
    private LocalDateTime endTime;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private BookingStatus status;

    @Enumerated(EnumType.STRING)
    @Column(name = "tariff", nullable = false)
    private BookingTariff tariff;

    @Column(name = "rental_cost", nullable = true)
    private BigDecimal rentalCost;

    @Column(name = "distance", nullable = true)
    private BigDecimal distance;

    @ManyToOne
    @JoinColumn(name = "start_location_id")
    private Location startLocation;

    @OneToOne
    @JoinColumn(name = "end_location_id")
    private Location endLocation;

    @OneToOne
    @JoinColumn(name = "insurance_id")
    private Insurance insurance;
}
