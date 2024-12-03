package com.brigada.carsh.dto.response;

import com.brigada.carsh.domain.booking.BookingStatus;
import com.brigada.carsh.domain.booking.BookingTariff;
import com.brigada.carsh.domain.insurance.InsuranceType;
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
public class BookingResponseDTO {
    private Long id;
    private Long carId;
    private Long userId;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private BookingStatus status;
    private BookingTariff tariff;
    private BigDecimal rentalCost;
    private BigDecimal distance;
    private BigDecimal startLatitude;
    private BigDecimal startLongitude;
    private BigDecimal endLatitude;
    private BigDecimal endLongitude;
    private InsuranceType insuranceType;
}
