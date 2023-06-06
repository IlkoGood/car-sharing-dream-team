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
import com.carsharing.service.UserService;
import com.stripe.model.checkout.Session;
import java.math.BigDecimal;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
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
    private static final String MANAGER = "MANAGER";
    private static final double FINE_MULTIPLIER = 1.2;
    private final PaymentProvider paymentProvider;
    private final PaymentService paymentService;
    private final RentalService rentalService;
    private final CarService carService;
    private final UserService userService;
    private final RequestDtoMapper<PaymentRequestDto, Payment> requestDtoMapper;
    private final ResponseDtoMapper<PaymentResponseDto, Payment> responseDtoMapper;

    @PostMapping
    @PreAuthorize("hasAnyAuthority('CUSTOMER', 'MANAGER')")
    public PaymentResponseDto createPaymentSession(Authentication authentication,
                                                   @RequestBody PaymentRequestDto dto) {
        boolean isManager = authentication.getAuthorities().stream()
            .anyMatch(role -> role.getAuthority().equals(MANAGER));
        Long authUserId = userService.findByEmail(authentication.getName()).getId();
        Payment payment = requestDtoMapper.mapToModel(dto);
        Rental rental = rentalService.getById(payment.getRental().getId());
        if (isManager || Objects.equals(authUserId, rental.getUser().getId())) {
            Car car = carService.getById(rental.getCar().getId());
            long daysRental = ChronoUnit.DAYS.between(rental.getRentalDate(),
                    rental.getReturnDate()) + 1;
            long daysActual = ChronoUnit.DAYS.between(rental.getRentalDate(),
                    rental.getActualReturnDate()) + 1;
            BigDecimal dailyFee = car.getDailyFee();
            BigDecimal moneyToPay = dailyFee.multiply(BigDecimal.valueOf(daysRental));
            BigDecimal moneyToFine = BigDecimal.ZERO;
            if (daysActual > daysRental) {
                moneyToFine = dailyFee.multiply(BigDecimal
                        .valueOf((daysActual - daysRental) * FINE_MULTIPLIER));
                payment.setType(Payment.Type.FINE);
            }
            payment.setAmount(moneyToPay.add(moneyToFine));
            payment = paymentService.save(payment);
            Session session = paymentProvider.createPaymentSession(
                    moneyToPay,
                    moneyToFine,
                    payment
            );
            payment.setSessionId(session.getId());
            payment.setSessionUrl(session.getUrl());
            return responseDtoMapper.mapToDto(paymentService.save(payment));
        }
        throw new RuntimeException("You do not have access to payment by rentalId: " + rental.getId());
    }

    @GetMapping
    @PreAuthorize("hasAnyAuthority('CUSTOMER', 'MANAGER')")
    public List<PaymentResponseDto> getByUserId(Authentication authentication,
                                                @RequestParam(required = false) Long userId) {
        boolean isManager = authentication.getAuthorities().stream()
            .anyMatch(role -> role.getAuthority().equals(MANAGER));
        Long authUserId = userService.findByEmail(authentication.getName()).getId();
        if (isManager && userId == null) {
            return paymentService.getAll().stream()
                    .map(responseDtoMapper::mapToDto)
                    .collect(Collectors.toList());
        }
        if (isManager || Objects.equals(authUserId, userId)) {
            return paymentService.getByUserId(userId).stream()
                    .map(responseDtoMapper::mapToDto)
                    .collect(Collectors.toList());
        }
        throw new RuntimeException("You do not have access to payment by user id: " + userId);
    }

    @GetMapping("success/{id}")
    public RedirectView getSucceed(@PathVariable Long id) {
        Payment payment = paymentService.getById(id);
        payment.setReceiptUrl(paymentProvider.getCharge(payment.getSessionId()).getReceiptUrl());
        payment.setSessionUrl(null);
        payment.setStatus(Payment.Status.PAID);
        return new RedirectView(paymentService.save(payment).getReceiptUrl());
    }

    @GetMapping("cancel/{id}")
    public RedirectView getCanceled(@PathVariable Long id) {
        return new RedirectView(paymentService.getById(id).getSessionUrl());
    }
}
