package com.brigada.carsh.mapper;

import com.brigada.carsh.domain.supportticket.SupportTicket;
import com.brigada.carsh.dto.request.SupportTicketRequestDTO;
import com.brigada.carsh.dto.response.SupportTicketResponseDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.factory.Mappers;

@Mapper
public interface SupportTicketMapper {
    SupportTicketMapper INSTANCE = Mappers.getMapper(SupportTicketMapper.class);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "user", ignore = true)
    @Mapping(target = "booking", ignore = true)
    @Mapping(target = "status", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    SupportTicket toEntity(SupportTicketRequestDTO requestDTO);

    @Mapping(target = "userId", source = "user.id")
    @Mapping(target = "bookingId", source = "booking.id")
    SupportTicketResponseDTO toResponseDTO(SupportTicket e);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "user", ignore = true)
    @Mapping(target = "booking", ignore = true)
    @Mapping(target = "status", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    void updateSupportTicket(SupportTicketRequestDTO requestDTO, @MappingTarget SupportTicket e);
}
