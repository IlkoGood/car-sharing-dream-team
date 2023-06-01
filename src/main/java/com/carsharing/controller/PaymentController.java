package com.carsharing.controller;

import com.carsharing.api.stripe.PaymentProvider;
import com.carsharing.dto.mapper.RequestDtoMapper;
import com.carsharing.dto.mapper.ResponseDtoMapper;
import com.carsharing.dto.request.PaymentRequestDto;
import com.carsharing.dto.response.PaymentResponseDto;
import com.carsharing.model.Payment;
import com.carsharing.model.Rental;
import com.carsharing.service.PaymentService;
import com.stripe.model.checkout.Session;
import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@AllArgsConstructor
@RestController
@RequestMapping("payments")
public class PaymentController {
    private final PaymentProvider paymentProvider;
    private final PaymentService paymentService;
    private final RequestDtoMapper<PaymentRequestDto, Payment> requestDtoMapper;
    private final ResponseDtoMapper<PaymentResponseDto, Payment> responseDtoMapper;

    @PostMapping
    public PaymentResponseDto createPaymentSession(@RequestBody PaymentRequestDto dto) {
        //Payment payment = requestDtoMapper.mapToModel(dto);
        Payment payment = new Payment();
        payment.setRental(new Rental());
        payment.setType(dto.getType());
        payment.setStatus(Payment.Status.PENDING);
        payment.setAmount(BigDecimal.valueOf(200L));
        payment = paymentService.save(payment);
        Session session = paymentProvider.createPaymentSession(
                payment.getAmount().longValue(),
                payment.getType()
        );
        session.setSuccessUrl(session.getSuccessUrl() + payment.getId());
        session.setCancelUrl(session.getCancelUrl() + payment.getId());
        payment.setSessionId(session.getId());
        payment.setSessionUrl(session.getUrl());
        payment = paymentService.save(payment);
        return responseDtoMapper.mapToDto(payment);
    }

    @GetMapping
    @PreAuthorize("hasAnyAuthority('CUSTOMER', 'MANAGER')")
    public List<PaymentResponseDto> getByUserId(@RequestParam Long userId) {
        return paymentService.getByUserId(userId).stream()
                .map(responseDtoMapper::mapToDto)
                .collect(Collectors.toList());
    }

    @GetMapping("success/{id}")
    @PreAuthorize("hasAnyAuthority('CUSTOMER', 'MANAGER')")
    public PaymentResponseDto getSucceed(@PathVariable Long id) {
        Payment payment = paymentService.getById(id);
        payment.setStatus(Payment.Status.PAID);
        return responseDtoMapper.mapToDto(paymentService.save(payment));
    }

    @GetMapping("cancel/{id}")
    @PreAuthorize("hasAnyAuthority('CUSTOMER', 'MANAGER')")
    public PaymentResponseDto getCanceled(@PathVariable Long id) {
        return responseDtoMapper.mapToDto(paymentService.save(paymentService.getById(id)));
    }
}
