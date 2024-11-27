package com.brigada.carsh.service;

import com.brigada.carsh.domain.car.Car;
import com.brigada.carsh.domain.car.CarStatus;
import com.brigada.carsh.domain.maintenance.Maintenance;
import com.brigada.carsh.domain.maintenance.MaintenanceStatus;
import com.brigada.carsh.dto.request.MaintenanceRequestDTO;
import com.brigada.carsh.dto.response.MaintenanceResponseDTO;
import com.brigada.carsh.exception.CarNotFoundException;
import com.brigada.carsh.exception.InconsistentRequestException;
import com.brigada.carsh.exception.MaintenanceNotFoundException;
import com.brigada.carsh.mapper.MaintenanceMapper;
import com.brigada.carsh.repository.CarRepository;
import com.brigada.carsh.repository.MaintenanceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class MaintenanceService {
    private final MaintenanceRepository maintenanceRepository;
    private final CarRepository carRepository;

    public List<MaintenanceResponseDTO> getAllMaintenanceByCarId(Long carId) {
        if (!carRepository.existsById(carId)) {
            throw new CarNotFoundException(String.format("Car with id=%s was not found", carId));
        }
        return maintenanceRepository.findAllByCarId(carId).stream().map(MaintenanceMapper.INSTANCE::toResponseDTO).toList();
    }

    public MaintenanceResponseDTO addMaintenance(MaintenanceRequestDTO maintenanceRequestDTO, Long carId) {
        Car car = carRepository.findById(carId).orElseThrow(() -> new CarNotFoundException(String.format("Car with id=%s was not found", carId)));
        if (!(car.getStatus() == CarStatus.AVAILABLE || car.getStatus() == CarStatus.RENTED)) {
            throw new InconsistentRequestException(String.format("Car with id=%s cannot be sent to maintenance", carId));
        }
        Maintenance maintenance = MaintenanceMapper.INSTANCE.toEntity(maintenanceRequestDTO);
        maintenance.setCar(car);
        maintenance.setStartDate(LocalDateTime.now());
        maintenance.setStatus(MaintenanceStatus.PENDING);
        car.setStatus(CarStatus.MAINTENANCE);
        carRepository.save(car);
        return MaintenanceMapper.INSTANCE.toResponseDTO(maintenanceRepository.save(maintenance));
    }

    public MaintenanceResponseDTO updateMaintenance(MaintenanceRequestDTO maintenanceRequestDTO, Long id) {
        Maintenance maintenance = maintenanceRepository.findById(id).orElseThrow(() -> new MaintenanceNotFoundException(String.format("Maintenance with id=%s was not found", id)));
        MaintenanceMapper.INSTANCE.updateMaintenance(maintenanceRequestDTO, maintenance);
        return MaintenanceMapper.INSTANCE.toResponseDTO(maintenanceRepository.save(maintenance));
    }

    public MaintenanceResponseDTO endMaintenance(Long id) {
        Maintenance maintenance = maintenanceRepository.findById(id).orElseThrow(() -> new MaintenanceNotFoundException(String.format("Maintenance with id=%s was not found", id)));
        maintenance.setEndDate(LocalDateTime.now());
        maintenance.setStatus(MaintenanceStatus.COMPLETED);
        return MaintenanceMapper.INSTANCE.toResponseDTO(maintenanceRepository.save(maintenance));
    }

    public void deleteMaintenance(Long id) {
        maintenanceRepository.deleteById(id);
    }
}
