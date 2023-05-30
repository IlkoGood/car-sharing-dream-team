package com.carsharing.controller;

import com.carsharing.dto.mapper.RequestDtoMapper;
import com.carsharing.dto.mapper.ResponseDtoMapper;
import com.carsharing.dto.request.CarRequestDto;
import com.carsharing.dto.response.CarResponseDto;
import com.carsharing.model.Car;
import com.carsharing.service.CarService;
import java.util.List;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@AllArgsConstructor
@RestController
@RequestMapping("/cars")
public class CarController {
    private final CarService carService;
    private final RequestDtoMapper<CarRequestDto, Car> requestDtoMapper;
    private final ResponseDtoMapper<CarResponseDto, Car> responseDtoMapper;

    @PostMapping
    public CarResponseDto create(@RequestBody CarRequestDto carRequestDto) {
        return responseDtoMapper.mapToDto(carService
                .save(requestDtoMapper.mapToModel(carRequestDto)));
    }

    @GetMapping("/{id}")
    public CarResponseDto get(@PathVariable Long id) {
        return responseDtoMapper.mapToDto(carService.getById(id));
    }

    @PutMapping("/{id}")
    public CarResponseDto update(@PathVariable Long id, @RequestBody CarRequestDto requestDto) {
        Car car = requestDtoMapper.mapToModel(requestDto);
        car.setId(id);
        Car updateCar = carService.update(car);
        return responseDtoMapper.mapToDto(updateCar);
    }

    @DeleteMapping("/{id}")
    void delete(@PathVariable Long id) {
        carService.delete(id);
    }

    @GetMapping
    public List<CarResponseDto> getAll() {
        return carService.getAll().stream()
                .map(responseDtoMapper::mapToDto)
                .collect(Collectors.toList());
    }
}
