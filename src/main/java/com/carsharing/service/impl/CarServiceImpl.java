package com.carsharing.service.impl;

import com.carsharing.model.Car;
import com.carsharing.repository.CarRepository;
import com.carsharing.service.CarService;
import java.util.List;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class CarServiceImpl implements CarService {
    private final CarRepository carRepository;

    @Override
    public Car save(Car car) {
        return carRepository.save(car);
    }

    @Override
    public Car getById(Long id) {
        return carRepository.findById(id).orElseThrow(() ->
                new RuntimeException("Can't find car by id: " + id));
    }

    @Override
    public Car update(Car car) {
        if (carRepository.existsById(car.getId())) {
            return carRepository.save(car);
        }
        throw new RuntimeException("Can't find car by id: " + car.getId());
    }

    @Override
    public void delete(Long id) {
        carRepository.deleteById(id);
    }

    @Override
    public List<Car> getAll() {
        return carRepository.findAll();
    }
}
