package com.carsharing.service;

import com.carsharing.model.Rental;

public interface NotificationService {
    void sendNotification(Rental rental);
}
