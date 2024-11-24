package com.brigada.carsh.repository;

import com.brigada.carsh.domain.car.Car;
import com.brigada.carsh.domain.car.CarStatus;
import com.brigada.carsh.dto.response.CarResponseDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.util.List;

public interface CarRepository extends JpaRepository<Car, Long> {
    List<Car> getAllByStatus(CarStatus status);

    @Query(nativeQuery = true,
            name = "available_cars_by_radius")
    List<CarResponseDTO> getAvailableCarsByRadius(
            @Param("latitude") BigDecimal latitude,
            @Param("longitude") BigDecimal longitude,
            @Param("radius") int radius);
}
