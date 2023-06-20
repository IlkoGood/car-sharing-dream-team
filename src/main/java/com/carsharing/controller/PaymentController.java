package com.carsharing.controller;

import com.carsharing.dto.mapper.RequestDtoMapper;
import com.carsharing.dto.mapper.ResponseDtoMapper;
import com.carsharing.dto.request.PaymentRequestDto;
import com.carsharing.dto.response.PaymentResponseDto;
import com.carsharing.model.Car;
import com.carsharing.model.Payment;
import com.carsharing.model.Rental;
import com.carsharing.security.AccessService;
import com.carsharing.service.CarService;
import com.carsharing.service.PaymentService;
import com.carsharing.service.RentalService;
import java.util.List;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.view.RedirectView;

@AllArgsConstructor
@RestController
@RequestMapping("payments")
public class PaymentController {
    private final PaymentService paymentService;
    private final RentalService rentalService;
    private final CarService carService;
    private final AccessService accessService;
    private final RequestDtoMapper<PaymentRequestDto, Payment> requestDtoMapper;
    private final ResponseDtoMapper<PaymentResponseDto, Payment> responseDtoMapper;

    @PostMapping
    public PaymentResponseDto createPaymentSession(Authentication authentication,
                                                   @RequestBody PaymentRequestDto dto) {
        Payment payment = requestDtoMapper.mapToModel(dto);
        Rental rental = rentalService.getById(payment.getRentalId());
        if (accessService.checkUserAccess(authentication, rental.getUserId())) {
            throw new RuntimeException("You do not have access to this data");
        }
        Car car = carService.getById(rental.getCarId());
        payment = paymentService.createPaymentSession(payment, rental, car);
        return responseDtoMapper.mapToDto(paymentService.save(payment));
    }

    @GetMapping
    public List<PaymentResponseDto> getPaymentByUserId(Authentication authentication,
                                                @RequestParam(required = false) Long userId) {
        if (accessService.checkUserAccess(authentication, userId)) {
            throw new RuntimeException("You do not have access to this data");
        }
        return paymentService.getByUserId(userId).stream()
                .map(responseDtoMapper::mapToDto)
                .collect(Collectors.toList());
    }

    @GetMapping("success/{id}")
    public RedirectView getSucceedRedirection(@PathVariable Long id) {
        Payment payment = paymentService.getById(id);
        payment.setReceiptUrl(paymentService.getReceiptUrl(payment.getSessionId()));
        payment.setSessionUrl(null);
        payment.setStatus(Payment.Status.PAID);
        return new RedirectView(paymentService.save(payment).getReceiptUrl());
    }

    @GetMapping("cancel/{id}")
    public RedirectView getCanceledRedirection(@PathVariable Long id) {
        return new RedirectView(paymentService.getById(id).getSessionUrl());
    }
}
