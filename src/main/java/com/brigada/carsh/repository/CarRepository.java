package com.brigada.carsh.repository;

import com.brigada.carsh.domain.booking.BookingStatus;
import com.brigada.carsh.domain.car.Car;
import com.brigada.carsh.domain.car.CarStatus;
import com.brigada.carsh.dto.response.CarResponseDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

@Repository
public interface CarRepository extends JpaRepository<Car, Long> {
    List<Car> getAllByStatus(CarStatus status);

    @Query(nativeQuery = true,
            name = "available_cars_by_radius")
    List<CarResponseDTO> getAvailableCarsByRadius(
            @Param("latitude") BigDecimal latitude,
            @Param("longitude") BigDecimal longitude,
            @Param("radius") int radius);

    @Query("SELECT c FROM Car c WHERE c.id IN " +
            "(SELECT b.car.id FROM Booking b WHERE b.status = :status)")
    List<Car> getCarsByBookingStatus(@Param("status") BookingStatus status);
}
