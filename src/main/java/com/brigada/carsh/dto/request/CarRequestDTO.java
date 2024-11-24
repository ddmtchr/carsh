package com.brigada.carsh.dto.request;

import com.brigada.carsh.domain.car.CarClass;
import com.brigada.carsh.domain.car.CarStatus;
import com.brigada.carsh.domain.location.Location;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class CarRequestDTO {
    private String registrationNumber;
    private String model;
    private CarClass carClass;
    private BigDecimal fuelLevel;
    private BigDecimal minutePrice;
    private Location location;
    private CarStatus status;

}
