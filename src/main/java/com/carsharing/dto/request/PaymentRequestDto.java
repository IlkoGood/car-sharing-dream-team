package com.carsharing.dto.request;

import com.carsharing.model.Payment;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class PaymentRequestDto {
    @Min(value = 1)
    @NotNull
    private Long rentalId;
    @Enum
    @NotNull
    private Payment.Type type;
}
