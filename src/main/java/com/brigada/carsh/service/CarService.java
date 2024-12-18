package com.brigada.carsh.service;

import com.brigada.carsh.domain.car.Car;
import com.brigada.carsh.domain.car.CarStatus;
import com.brigada.carsh.dto.request.CarRequestDTO;
import com.brigada.carsh.dto.response.CarResponseDTO;
import com.brigada.carsh.exception.CarNotFoundException;
import com.brigada.carsh.mapper.CarMapper;
import com.brigada.carsh.repository.CarRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

    public CarResponseDTO getCarById(Long id) {
        return carRepository.findById(id).map(CarMapper.INSTANCE::toResponseDTO).orElseThrow(() ->
                new CarNotFoundException(String.format("Car with id=%s was not found", id)));
    }

    @Transactional
    public CarResponseDTO addCar(CarRequestDTO carRequestDTO) {
        Car car = carRepository.save(CarMapper.INSTANCE.toEntity(carRequestDTO));
        return CarMapper.INSTANCE.toResponseDTO(car);
    }

    @Transactional
    public CarResponseDTO updateCar(CarRequestDTO carRequestDTO, Long id) {
        Car car = carRepository.findById(id).orElseThrow(() -> new CarNotFoundException(String.format("Car with id=%s was not found", id)));
        CarMapper.INSTANCE.updateCar(carRequestDTO, car);
        return CarMapper.INSTANCE.toResponseDTO(carRepository.save(car));
    }
}
