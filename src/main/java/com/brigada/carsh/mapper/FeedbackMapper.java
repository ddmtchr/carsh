package com.brigada.carsh.mapper;

import com.brigada.carsh.domain.feedback.Feedback;
import com.brigada.carsh.dto.request.FeedbackRequestDTO;
import com.brigada.carsh.dto.response.FeedbackResponseDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.factory.Mappers;

@Mapper
public interface FeedbackMapper {
    FeedbackMapper INSTANCE = Mappers.getMapper(FeedbackMapper.class);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "booking", ignore = true)
    @Mapping(target = "date", ignore = true)
    Feedback toEntity(FeedbackRequestDTO requestDTO);

    @Mapping(target = "bookingId", source = "booking.id")
    FeedbackResponseDTO toResponseDTO(Feedback e);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "booking", ignore = true)
    @Mapping(target = "date", ignore = true)
    void updateFeedback(FeedbackRequestDTO requestDTO, @MappingTarget Feedback e);

}
