package com.carsharing.dto.mapper.impl;

import com.carsharing.dto.mapper.RequestDtoMapper;
import com.carsharing.dto.mapper.ResponseDtoMapper;
import com.carsharing.dto.request.PaymentRequestDto;
import com.carsharing.dto.response.PaymentResponseDto;
import com.carsharing.model.Payment;
import com.carsharing.service.RentalService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

@AllArgsConstructor
@Component
public class PaymentMapper implements ResponseDtoMapper<PaymentResponseDto, Payment>,
        RequestDtoMapper<PaymentRequestDto, Payment> {
    private final RentalService rentalService;

    @Override
    public PaymentResponseDto mapToDto(Payment payment) {
        PaymentResponseDto dto = new PaymentResponseDto();
        dto.setId(payment.getId());
        dto.setStatus(payment.getStatus());
        dto.setType(payment.getType());
        dto.setRental(payment.getRental().getId());
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
        payment.setRental(rentalService.getById(dto.getRental()));
        return payment;
    }
}
