package com.brigada.carsh.mapper;

import com.brigada.carsh.domain.payment.Payment;
import com.brigada.carsh.dto.request.PaymentRequestDTO;
import com.brigada.carsh.dto.response.PaymentResponseDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.factory.Mappers;

@Mapper
public interface PaymentMapper {
    PaymentMapper INSTANCE = Mappers.getMapper(PaymentMapper.class);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "booking", ignore = true)
    @Mapping(target = "paymentDate", ignore = true)
    Payment toEntity(PaymentRequestDTO requestDTO);

    @Mapping(target = "bookingId", source = "booking.id")
    PaymentResponseDTO toResponseDTO(Payment e);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "booking", ignore = true)
    @Mapping(target = "paymentDate", ignore = true)
    void updatePayment(PaymentRequestDTO requestDTO, @MappingTarget Payment e);

}
