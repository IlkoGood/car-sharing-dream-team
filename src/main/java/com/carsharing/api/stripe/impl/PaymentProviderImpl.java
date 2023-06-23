package com.carsharing.api.stripe.impl;

import com.carsharing.api.stripe.PaymentProvider;
import com.carsharing.model.Car;
import com.carsharing.model.Payment;
import com.carsharing.model.Rental;
import com.carsharing.model.User;
import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.Charge;
import com.stripe.model.PaymentIntent;
import com.stripe.model.checkout.Session;
import com.stripe.param.checkout.SessionCreateParams;
import java.math.BigDecimal;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class PaymentProviderImpl implements PaymentProvider {
    private static final String GROUP = "OOO `Car-Sharing inc.";
    private static final String CURRENCY = "usd";
    private static final Long QUANTITY = 1L;
    private static final Long STRIPE_PRICE_MULTIPLIER = 100L;
    @Value("${stripe.secret}")
    private String secretKey;
    @Value("${app.domen}")
    private String domen;

    public Session createPaymentSession(BigDecimal payment,
                                        BigDecimal fine,
                                        Payment paymentObject,
                                        Rental rental,
                                        Car car,
                                        User user) {
        Stripe.apiKey = secretKey;
        Long paymentId = paymentObject.getId();
        SessionCreateParams.Builder paramsBuilder = SessionCreateParams.builder()
                .setPaymentIntentData(SessionCreateParams.PaymentIntentData.builder()
                        .setDescription("Car-sharing payment[id: " + paymentObject.getId() + "] for "
                                + car.getBrand() + ' ' + car.getModel() + " rental")
                        .setTransferGroup(GROUP)
                        .build()
                )
                .setCustomerEmail(user.getEmail())
                .setMode(SessionCreateParams.Mode.PAYMENT)
                .setSuccessUrl(domen + "/payments/success/" + paymentId)
                .setCancelUrl(domen + "/payments/cancel/" + paymentId)
                .addPaymentMethodType(SessionCreateParams.PaymentMethodType.CARD)
                .addLineItem(
                        SessionCreateParams.LineItem.builder()
                                .setQuantity(QUANTITY)
                                .setPriceData(
                                        SessionCreateParams.LineItem.PriceData.builder()
                                                .setCurrency(CURRENCY)
                                                .setUnitAmount((long) (payment.doubleValue()
                                                        * STRIPE_PRICE_MULTIPLIER))
                                                .setProductData(
                                                        SessionCreateParams.LineItem.PriceData
                                                                .ProductData.builder()
                                                                .setName(
                                                                        Payment.Type.PAYMENT.name()
                                                                )
                                                                .build()
                                                )
                                                .build()
                                )
                                .build()
                );
        if (fine.doubleValue() > 0) {
            paramsBuilder.addLineItem(
                    SessionCreateParams.LineItem.builder()
                            .setQuantity(QUANTITY)
                            .setPriceData(
                                    SessionCreateParams.LineItem.PriceData.builder()
                                            .setCurrency(CURRENCY)
                                            .setUnitAmount((long) (fine.doubleValue()
                                                    * STRIPE_PRICE_MULTIPLIER))
                                            .setProductData(
                                                    SessionCreateParams.LineItem
                                                            .PriceData.ProductData.builder()
                                                            .setName(Payment.Type.FINE.name())
                                                            .build()
                                            )
                                            .build()
                            )
                            .build()
            );
        }
        SessionCreateParams params = paramsBuilder.build();
        try {
            return Session.create(params);
        } catch (StripeException e) {
            throw new RuntimeException("Can`t create session with params: " + params, e);
        }
    }

    public Charge getCharge(String sessionId) {
        try {
            Session session = Session.retrieve(sessionId);
            PaymentIntent paymentIntent = PaymentIntent.retrieve(session.getPaymentIntent());
            return Charge.retrieve(paymentIntent.getLatestCharge());
        } catch (StripeException e) {
            throw new RuntimeException("Something went wrong, when we try to find receipt by session id: "
                    + sessionId);
        }
    }
}
