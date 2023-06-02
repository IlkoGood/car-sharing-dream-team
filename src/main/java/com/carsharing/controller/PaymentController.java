package com.carsharing.controller;

import com.carsharing.api.stripe.PaymentProvider;
import com.carsharing.dto.mapper.RequestDtoMapper;
import com.carsharing.dto.mapper.ResponseDtoMapper;
import com.carsharing.dto.request.PaymentRequestDto;
import com.carsharing.dto.response.PaymentResponseDto;
import com.carsharing.model.Car;
import com.carsharing.model.Payment;
import com.carsharing.model.Rental;
import com.carsharing.service.CarService;
import com.carsharing.service.PaymentService;
import com.carsharing.service.RentalService;
import com.stripe.model.checkout.Session;
import java.math.BigDecimal;
import java.time.temporal.ChronoUnit;
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
    private static final double FINE_MULTIPLIER = 1.2;
    private final PaymentProvider paymentProvider;
    private final PaymentService paymentService;
    private final RentalService rentalService;
    private final CarService carService;
    private final RequestDtoMapper<PaymentRequestDto, Payment> requestDtoMapper;
    private final ResponseDtoMapper<PaymentResponseDto, Payment> responseDtoMapper;


    @PostMapping
    @PreAuthorize("hasAnyAuthority('CUSTOMER', 'MANAGER')")
    public PaymentResponseDto createPaymentSession(@RequestBody PaymentRequestDto dto) {
        Payment payment = requestDtoMapper.mapToModel(dto);
        Rental rental = rentalService.getById(payment.getRental().getId());
        Car car = carService.getById(rental.getCar().getId());
        long daysRental = ChronoUnit.DAYS.between(rental.getRentalDate(),
                rental.getReturnDate()) + 1;
        long daysActual = ChronoUnit.DAYS.between(rental.getRentalDate(),
                rental.getActualReturnDate()) + 1;
        BigDecimal dailyFee = car.getDailyFee();
        BigDecimal moneyToPay = dailyFee.multiply(BigDecimal.valueOf(daysRental));
        BigDecimal moneyToFine = BigDecimal.ZERO;
        if (daysActual > daysRental) {
            moneyToFine = dailyFee.multiply(BigDecimal.valueOf((daysActual - daysRental) * FINE_MULTIPLIER));
        }
        payment.setAmount(moneyToPay);
        payment = paymentService.save(payment);
        Session session = paymentProvider.createPaymentSession(
                payment.getAmount(),
                moneyToFine,
                payment
        );
        payment.setSessionId(session.getId());
        payment.setSessionUrl(session.getUrl());
        return responseDtoMapper.mapToDto(paymentService.save(payment));
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
