package com.carsharing.api.stripe;

import com.carsharing.model.Payment;
import com.stripe.model.checkout.Session;

public interface PaymentProvider {
    Session createPaymentSession(Long amount, Payment.Type type);
}
