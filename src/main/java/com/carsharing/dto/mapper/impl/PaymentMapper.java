package com.carsharing.dto.mapper.impl;

import com.carsharing.dto.mapper.ResponseDtoMapper;
import com.carsharing.dto.response.PaymentResponseDto;
import com.carsharing.model.Payment;
import org.springframework.stereotype.Component;

@Component
public class PaymentMapper implements ResponseDtoMapper<PaymentResponseDto, Payment> {
    @Override
    public PaymentResponseDto mapToDto(Payment payment) {
        PaymentResponseDto dto = new PaymentResponseDto();
        dto.setId(payment.getId());
        dto.setStatus(payment.getStatus());
        dto.setType(payment.getType());
        dto.setRental(payment.getRental().getId());
        dto.setSessionUrl(payment.getSessionUrl());
        dto.setSessionId(payment.getSessionId());
        dto.setAmount(payment.getAmount());
        return dto;
    }
}
