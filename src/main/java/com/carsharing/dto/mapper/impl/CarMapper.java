package com.carsharing.dto.mapper.impl;

import com.carsharing.dto.mapper.RequestDtoMapper;
import com.carsharing.dto.mapper.ResponseDtoMapper;
import com.carsharing.dto.request.CarRequestDto;
import com.carsharing.dto.response.CarResponseDto;
import com.carsharing.model.Car;

public class CarMapper implements ResponseDtoMapper<CarResponseDto, Car>,
        RequestDtoMapper<CarRequestDto, Car> {
    @Override
    public Car mapToModel(CarRequestDto dto) {
        Car car = new Car();
        car.setModel(dto.getModel());
        car.setBrand(dto.getBrand());
        car.setType(dto.getType());
        car.setInventory(dto.getInventory());
        car.setDailyFee(dto.getDailyFee());
        return car;
    }

    @Override
    public CarResponseDto mapToDto(Car car) {
        CarResponseDto carResponseDto = new CarResponseDto();
        carResponseDto.setId(car.getId());
        carResponseDto.setModel(car.getModel());
        carResponseDto.setBrand(car.getBrand());
        carResponseDto.setType(car.getType());
        car.setInventory(car.getInventory());
        car.setDailyFee(car.getDailyFee());
        return carResponseDto;
    }
}
