package com.brigada.carsh.domain.insurance;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Entity
@Table(name = "insurance")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Insurance {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "insurance_type", nullable = false)
    private InsuranceType insuranceType;

    @Column(name = "coverage", nullable = false)
    private String coverage;

    @Column(name = "cost_ratio", nullable = false)
    private BigDecimal costRatio;
}
