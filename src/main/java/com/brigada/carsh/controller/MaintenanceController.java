package com.brigada.carsh.controller;

import com.brigada.carsh.dto.request.MaintenanceRequestDTO;
import com.brigada.carsh.dto.response.MaintenanceResponseDTO;
import com.brigada.carsh.service.MaintenanceService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/maintenance")
@RequiredArgsConstructor
public class MaintenanceController {
    private final MaintenanceService maintenanceService;

    @GetMapping("/car/{carId}")
    public ResponseEntity<List<MaintenanceResponseDTO>> getAllMaintenanceByCarId(@PathVariable Long carId) {
        return ResponseEntity.ok(maintenanceService.getAllMaintenanceByCarId(carId));
    }

    @PostMapping("/car/{carId}")
    public ResponseEntity<MaintenanceResponseDTO> addMaintenance(@RequestBody MaintenanceRequestDTO maintenanceRequestDTO, @PathVariable Long carId) {
        return new ResponseEntity<>(maintenanceService.addMaintenance(maintenanceRequestDTO, carId), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<MaintenanceResponseDTO> updateMaintenance(@RequestBody MaintenanceRequestDTO maintenanceRequestDTO, @PathVariable Long id) {
        return ResponseEntity.ok(maintenanceService.updateMaintenance(maintenanceRequestDTO, id));
    }

    @PutMapping("/end/{id}")
    public ResponseEntity<MaintenanceResponseDTO> endMaintenance(@PathVariable Long id) {
        return ResponseEntity.ok(maintenanceService.endMaintenance(id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMaintenance(@PathVariable Long id) {
        maintenanceService.deleteMaintenance(id);
        return ResponseEntity.noContent().build();
    }
}
