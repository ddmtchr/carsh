package com.brigada.carsh.mapper;

import com.brigada.carsh.domain.maintenance.Maintenance;
import com.brigada.carsh.dto.request.MaintenanceRequestDTO;
import com.brigada.carsh.dto.response.MaintenanceResponseDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.factory.Mappers;

@Mapper
public interface MaintenanceMapper {
    MaintenanceMapper INSTANCE = Mappers.getMapper(MaintenanceMapper.class);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "car", ignore = true)
    @Mapping(target = "startDate", ignore = true)
    @Mapping(target = "endDate", ignore = true)
    @Mapping(target = "status", ignore = true)
    Maintenance toEntity(MaintenanceRequestDTO requestDTO);

    @Mapping(target = "carId", source = "car.id")
    MaintenanceResponseDTO toResponseDTO(Maintenance e);

    void updateMaintenance(MaintenanceRequestDTO requestDTO, @MappingTarget Maintenance e);
}
