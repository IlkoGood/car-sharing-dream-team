package com.carsharing.dto.request;

import com.carsharing.model.Payment;
import lombok.Data;

@Data
public class PaymentRequestDto {
    private Long rental;
    private Payment.Type type;
}
