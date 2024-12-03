package com.brigada.carsh.mapper;

import com.brigada.carsh.domain.booking.Booking;
import com.brigada.carsh.domain.car.Car;
import com.brigada.carsh.dto.request.BookingStartDTO;
import com.brigada.carsh.dto.response.BookingResponseDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.factory.Mappers;

@Mapper
public interface BookingMapper {
    BookingMapper INSTANCE = Mappers.getMapper(BookingMapper.class);

    @Mapping(target = "id", ignore = true)
    Car toEntity(BookingStartDTO requestDTO);

    @Mapping(target = "carId", source = "car.id")
    @Mapping(target = "userId", source = "user.id")
    @Mapping(target = "startLatitude", source = "startLocation.latitude")
    @Mapping(target = "startLongitude", source = "startLocation.longitude")
    @Mapping(target = "endLatitude", source = "endLocation.latitude")
    @Mapping(target = "endLongitude", source = "endLocation.longitude")
    @Mapping(target = "insuranceType", source = "insurance.insuranceType")
    BookingResponseDTO toResponseDTO(Booking e);

    void updateBooking(BookingStartDTO requestDTO, @MappingTarget Booking booking);
}
