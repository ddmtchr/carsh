package com.brigada.carsh.mapper;

import com.brigada.carsh.domain.documentverification.DocumentVerification;
import com.brigada.carsh.dto.request.DocumentVerificationRequestDTO;
import com.brigada.carsh.dto.response.DocumentVerificationResponseDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.factory.Mappers;


@Mapper
public interface DocumentVerificationMapper {
    DocumentVerificationMapper INSTANCE = Mappers.getMapper(DocumentVerificationMapper.class);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "user", ignore = true)
    @Mapping(target = "status", ignore = true)
    @Mapping(target = "verificationDate", ignore = true)
    DocumentVerification toEntity(DocumentVerificationRequestDTO requestDTO);

    @Mapping(target = "userId", source = "user.id")
    DocumentVerificationResponseDTO toResponseDTO(DocumentVerification e);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "user", ignore = true)
    @Mapping(target = "status", ignore = true)
    @Mapping(target = "verificationDate", ignore = true)
    void updateDocumentVerification(DocumentVerificationRequestDTO requestDTO, @MappingTarget DocumentVerification e);

}
