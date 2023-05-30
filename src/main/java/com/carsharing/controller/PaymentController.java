package com.carsharing.controller;

import com.carsharing.dto.response.PaymentResponseDto;
import java.util.Collections;
import java.util.List;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@AllArgsConstructor
@RestController
@RequestMapping("payments")
public class PaymentController {
    @PostMapping
    public void createPaymentSession() {
        //TODO create session on stripe
    }

    @GetMapping
    public List<PaymentResponseDto> getByUserId(@RequestParam Long userId) {
        return Collections.emptyList();
    }

    @GetMapping("success")
    public List<PaymentResponseDto> getSucceed() {
        return Collections.emptyList();
    }

    @GetMapping("cancel")
    public List<PaymentResponseDto> getCanceled() {
        return Collections.emptyList();
    }
}
