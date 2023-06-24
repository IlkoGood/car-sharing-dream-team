package com.carsharing.dto.request;

import com.carsharing.model.Payment;
import com.carsharing.validation.IsEnum;
import com.carsharing.validation.IsNumber;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

@Data
public final class PaymentRequestDto {
    @NotNull
    @IsNumber
    @Positive
    private String rentalId;
    @NotNull
    @IsEnum(enumClazz = Payment.Type.class, message = "must be like [PAYMENT, FINE]")
    private String type;
}
