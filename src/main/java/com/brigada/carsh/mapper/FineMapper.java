package com.brigada.carsh.mapper;

import com.brigada.carsh.domain.fine.Fine;
import com.brigada.carsh.dto.request.FineRequestDTO;
import com.brigada.carsh.dto.response.FineResponseDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.factory.Mappers;

@Mapper
public interface FineMapper {
    FineMapper INSTANCE = Mappers.getMapper(FineMapper.class);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "supportTicket", ignore = true)
    @Mapping(target = "user", ignore = true)
    @Mapping(target = "issuedDate", ignore = true)
    @Mapping(target = "status", ignore = true)
    Fine toEntity(FineRequestDTO requestDTO);

    @Mapping(target = "userId", source = "user.id")
    @Mapping(target = "supportTicketId", source = "supportTicket.id")
    FineResponseDTO toResponseDTO(Fine e);

    @Mapping(target = "id", ignore = true)
    void updateFine(FineRequestDTO requestDTO, @MappingTarget Fine e);

}
