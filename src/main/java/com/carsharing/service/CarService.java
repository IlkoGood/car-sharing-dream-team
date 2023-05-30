package com.carsharing.service;

import com.carsharing.model.Car;
import java.util.List;

public interface CarService {
    Car save(Car car);

    Car getById(Long id);

    Car update(Car car);

    void delete(Long id);

    List<Car> getAll();
}
