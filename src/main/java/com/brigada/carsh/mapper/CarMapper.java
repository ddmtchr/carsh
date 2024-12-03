package com.brigada.carsh.mapper;

import com.brigada.carsh.domain.car.Car;
import com.brigada.carsh.dto.request.CarRequestDTO;
import com.brigada.carsh.dto.response.CarResponseDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.factory.Mappers;

@Mapper
public interface CarMapper {
    CarMapper INSTANCE = Mappers.getMapper(CarMapper.class);

    @Mapping(target = "id", ignore = true)
    Car toEntity(CarRequestDTO requestDTO);

    @Mapping(target = "latitude", source = "location.latitude")
    @Mapping(target = "longitude", source = "location.longitude")
    CarResponseDTO toResponseDTO(Car e);

    @Mapping(target = "id", ignore = true)
    void updateCar(CarRequestDTO requestDTO, @MappingTarget Car car);
}
