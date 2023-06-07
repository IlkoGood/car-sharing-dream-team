package com.carsharing.api.stripe;

import com.carsharing.model.Car;
import com.carsharing.model.Payment;
import com.carsharing.model.Rental;
import com.carsharing.model.User;
import com.stripe.model.Charge;
import com.stripe.model.checkout.Session;
import java.math.BigDecimal;

public interface PaymentProvider {
    Session createPaymentSession(BigDecimal payment,
                                 BigDecimal fine,
                                 Payment paymentObject,
                                 Rental rental,
                                 Car car,
                                 User user);

    Charge getCharge(String sessionId);
}
