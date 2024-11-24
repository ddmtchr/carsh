package com.brigada.carsh.controller;

import com.brigada.carsh.dto.request.CarRequestDTO;
import com.brigada.carsh.dto.response.CarResponseDTO;
import com.brigada.carsh.service.CarService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/api/cars")
@RequiredArgsConstructor
public class CarController {
    private final CarService carService;

    @GetMapping
    public ResponseEntity<List<CarResponseDTO>> getAllCars() {
        return ResponseEntity.ok(carService.getAllCars());
    }

    @GetMapping("/available")
    public ResponseEntity<List<CarResponseDTO>> getAllAvailableCars() {
        return ResponseEntity.ok(carService.getAllAvailableCars());
    }

    @GetMapping("/by-radius")
    @Operation(
            parameters = {
                    @Parameter(name = "latitude", description = "Широта", required = true, example = "60.078811"),
                    @Parameter(name = "longitude", description = "Долгота", required = true, example = "30.247230"),
                    @Parameter(name = "radius", description = "Радиус в км", required = true, example = "2")
            }
    )
    public ResponseEntity<List<CarResponseDTO>> getAllAvailableCarsByRadius(BigDecimal latitude, BigDecimal longitude, Integer radius) {
        return ResponseEntity.ok(carService.getAllAvailableCarsByRadius(latitude, longitude, radius));
    }

    @GetMapping("/{id}")
    public ResponseEntity<CarResponseDTO> getCarById(@PathVariable Long id) {
        return ResponseEntity.ok(carService.getCarById(id));
    }

    @PostMapping
    public ResponseEntity<CarResponseDTO> addCar(@RequestBody CarRequestDTO carRequestDTO) {
        return new ResponseEntity<>(carService.addCar(carRequestDTO), HttpStatus.CREATED);
    }
}
