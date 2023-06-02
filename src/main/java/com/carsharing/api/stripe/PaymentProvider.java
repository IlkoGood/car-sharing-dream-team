package com.carsharing.api.stripe;

import com.carsharing.model.Payment;
import com.stripe.model.checkout.Session;
import java.math.BigDecimal;

public interface PaymentProvider {
    Session createPaymentSession(BigDecimal payment, BigDecimal fine, Payment paymentObject);
}
