package com.brigada.carsh.dto.response;

import com.brigada.carsh.domain.maintenance.MaintenanceStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class MaintenanceResponseDTO {
    private Long id;
    private Long carId;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private String maintenanceType;
    private MaintenanceStatus status;
}

