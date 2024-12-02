package com.brigada.carsh.service;

import com.brigada.carsh.domain.booking.BookingStatus;
import com.brigada.carsh.domain.car.Car;
import com.brigada.carsh.domain.location.Location;
import com.brigada.carsh.repository.CarRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Random;

@Service
@RequiredArgsConstructor
public class CarMovementSimulator {
    private final CarRepository carRepository;

    @Transactional
    public void simulateMovement() {
        Random random = new Random();
        List<Car> activeCars = carRepository.getCarsByBookingStatus(BookingStatus.ACTIVE);
        double LATITUDE_BOUND = 0.0005;
        double LONGITUDE_BOUND = 0.001;
        for (Car car : activeCars) {
            var oldLat = car.getLocation().getLatitude();
            var oldLng = car.getLocation().getLongitude();
            var newLat = oldLat.add(BigDecimal.valueOf(randomDoubleByRange(random, -LATITUDE_BOUND, LATITUDE_BOUND)));
            var newLng = oldLng.add(BigDecimal.valueOf(randomDoubleByRange(random, -LONGITUDE_BOUND, LONGITUDE_BOUND)));
            if (car.getFuelLevel().doubleValue() > 0) {
                car.setLocation(Location.builder().latitude(newLat).longitude(newLng).build());
                car.setFuelLevel(car.getFuelLevel().subtract(BigDecimal.valueOf(0.01)));
            }
            carRepository.save(car);
        }
    }

    private double randomDoubleByRange(Random random, double min, double max) {
        return random.nextDouble() * (max - min) + min;
    }
}
