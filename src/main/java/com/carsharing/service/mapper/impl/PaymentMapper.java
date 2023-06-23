package com.carsharing.service.mapper.impl;

import com.carsharing.service.mapper.RequestDtoMapper;
import com.carsharing.service.mapper.ResponseDtoMapper;
import com.carsharing.dto.request.PaymentRequestDto;
import com.carsharing.dto.response.PaymentResponseDto;
import com.carsharing.model.Payment;
import org.springframework.stereotype.Component;

@Component
public class PaymentMapper implements ResponseDtoMapper<PaymentResponseDto, Payment>,
        RequestDtoMapper<PaymentRequestDto, Payment> {

    @Override
    public PaymentResponseDto mapToDto(Payment payment) {
        PaymentResponseDto dto = new PaymentResponseDto();
        dto.setId(payment.getId());
        dto.setStatus(payment.getStatus());
        dto.setType(payment.getType());
        dto.setRental(payment.getRentalId());
        dto.setSessionUrl(payment.getSessionUrl());
        dto.setReceiptUrl(payment.getReceiptUrl());
        dto.setSessionId(payment.getSessionId());
        dto.setAmount(payment.getAmount());
        return dto;
    }

    @Override
    public Payment mapToModel(PaymentRequestDto dto) {
        Payment payment = new Payment();
        payment.setStatus(Payment.Status.PENDING);
        payment.setType(dto.getType());
        payment.setRentalId(dto.getRentalId());
        return payment;
    }
}
