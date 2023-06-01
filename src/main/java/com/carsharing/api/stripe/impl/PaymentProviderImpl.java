package com.carsharing.api.stripe.impl;

import com.carsharing.api.stripe.PaymentProvider;
import com.carsharing.model.Payment;
import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.checkout.Session;
import com.stripe.param.checkout.SessionCreateParams;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class PaymentProviderImpl implements PaymentProvider {
    @Value("${stripe.secret}")
    private String secretKey;
    @Value("${stripe.domen}")
    private String domen;

    public Session createPaymentSession(Long amount, Payment.Type type) {
        Stripe.apiKey = secretKey;
        SessionCreateParams params = SessionCreateParams.builder()
                .setMode(SessionCreateParams.Mode.PAYMENT)
                .setSuccessUrl(domen + "/payments/success/")
                .setCancelUrl(domen + "/payments/cancel/")
                .addPaymentMethodType(SessionCreateParams.PaymentMethodType.CARD)
                .addLineItem(
                        SessionCreateParams.LineItem.builder()
                                .setQuantity(1L)
                                .setPriceData(
                                        SessionCreateParams.LineItem.PriceData.builder()
                                                .setCurrency("usd")
                                                .setUnitAmount(amount)
                                                .setProductData(
                                                        SessionCreateParams.LineItem
                                                                .PriceData.ProductData.builder()
                                                                .setName(type.name())
                                                                .build()
                                                )
                                                .build()
                                )
                                .build()
                )
                .build();
        try {
            return Session.create(params);
        } catch (StripeException e) {
            throw new RuntimeException("Can`t create session with params: " + params, e);
        }
    }
}
