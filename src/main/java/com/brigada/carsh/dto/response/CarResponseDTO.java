package com.brigada.carsh.dto.response;

import com.brigada.carsh.domain.car.CarClass;
import com.brigada.carsh.domain.car.CarStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class CarResponseDTO {
    private Long id;
    private String registrationNumber;
    private String model;
    private CarClass carClass;
    private BigDecimal fuelLevel;
    private BigDecimal minutePrice;
    private BigDecimal latitude;
    private BigDecimal longitude;
    private CarStatus status;
}
