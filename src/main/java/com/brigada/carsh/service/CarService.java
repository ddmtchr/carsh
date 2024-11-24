package com.brigada.carsh.service;

import com.brigada.carsh.domain.car.Car;
import com.brigada.carsh.domain.car.CarStatus;
import com.brigada.carsh.dto.response.CarResponseDTO;
import com.brigada.carsh.mapper.CarMapper;
import com.brigada.carsh.repository.CarRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CarService {
    private final CarRepository carRepository;

    public List<CarResponseDTO> getAllCars() {
        List<Car> cars = carRepository.findAll();
        return cars.stream().map(CarMapper.INSTANCE::toResponseDTO).toList();
    }

    public List<CarResponseDTO> getAllAvailableCars() {
        List<Car> cars = carRepository.getAllByStatus(CarStatus.AVAILABLE);
        return cars.stream().map(CarMapper.INSTANCE::toResponseDTO).toList();
    }

    public List<CarResponseDTO> getAllAvailableCarsByRadius(BigDecimal latitude, BigDecimal longitude, int radius) {
        return carRepository.getAvailableCarsByRadius(latitude, longitude, radius);
    }
}
