package com.brigada.carsh.mapper;

import com.brigada.carsh.domain.accidentreport.AccidentReport;
import com.brigada.carsh.dto.request.AccidentReportRequestDTO;
import com.brigada.carsh.dto.response.AccidentReportResponseDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.factory.Mappers;

@Mapper
public interface AccidentReportMapper {
    AccidentReportMapper INSTANCE = Mappers.getMapper(AccidentReportMapper.class);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "supportTicket", ignore = true)
    @Mapping(target = "reportDate", ignore = true)
    AccidentReport toEntity(AccidentReportRequestDTO requestDTO);

    @Mapping(target = "supportTicketId", source = "supportTicket.id")
    AccidentReportResponseDTO toResponseDTO(AccidentReport e);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "supportTicket", ignore = true)
    @Mapping(target = "reportDate", ignore = true)
    void updateAccidentReport(AccidentReportRequestDTO requestDTO, @MappingTarget AccidentReport e);

}
