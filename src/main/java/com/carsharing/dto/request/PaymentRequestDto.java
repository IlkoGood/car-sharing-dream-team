package com.carsharing.dto.request;

import com.carsharing.model.Payment;
import lombok.Data;

@Data
public class PaymentRequestDto {
    private Long rentalId;
    private Payment.Type type;
}
