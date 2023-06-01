package com.carsharing.controller;

import com.carsharing.dto.mapper.RequestDtoMapper;
import com.carsharing.dto.mapper.ResponseDtoMapper;
import com.carsharing.dto.request.RentalRequestDto;
import com.carsharing.dto.response.RentalResponseDto;
import com.carsharing.model.Car;
import com.carsharing.model.Rental;
import com.carsharing.service.CarService;
import com.carsharing.service.RentalService;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@AllArgsConstructor
@RestController
@RequestMapping("rentals")
public class RentalController {
    private final RentalService rentalService;
    private final CarService carService;
    private final RequestDtoMapper<RentalRequestDto, Rental> requestDtoMapper;
    private final ResponseDtoMapper<RentalResponseDto, Rental> responseDtoMapper;

    @PostMapping
    @PreAuthorize("hasAnyAuthority('CUSTOMER', 'MANAGER')")
    public RentalResponseDto createRental(@RequestBody RentalRequestDto requestDto) {
        Car car = carService.getById(requestDto.getCarId());
        if (car.getInventory() < 1) {
            return new RentalResponseDto();
        }
        car.setInventory(car.getInventory() - 1);
        carService.update(car);
        return responseDtoMapper.mapToDto(rentalService.save(requestDtoMapper.mapToModel(requestDto)));
    }

    @GetMapping
    @PreAuthorize("hasAnyAuthority('CUSTOMER', 'MANAGER')")
    public List<RentalResponseDto> getRentals(@RequestParam(required = false) Long userId,
                                              @RequestParam(required = false) Boolean isActive) {
        if (userId == null && isActive == null) {
            return rentalService.getAll().stream()
                    .map(responseDtoMapper::mapToDto)
                    .collect(Collectors.toList());
        }
        List<Rental> rentals = new ArrayList<>();
        if (userId != null) {
            rentals.addAll(rentalService.getAllByUserId(userId));
        }
        if (isActive != null) {
            rentals.addAll(rentalService.getActive(isActive)) ;
        }
        return rentals.stream()
                .map(responseDtoMapper::mapToDto)
                .collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('CUSTOMER', 'MANAGER')")
    public RentalResponseDto getById(@PathVariable Long id) {
        return responseDtoMapper.mapToDto(rentalService.getById(id));
    }

    @PutMapping("/{id}/return")
    @PreAuthorize("hasAnyAuthority('CUSTOMER', 'MANAGER')")
    public RentalResponseDto closeRental(@PathVariable Long id) {
        Rental rental = rentalService.getById(id);
        rental.setActualReturnDate(LocalDateTime.now());
        Car car = carService.getById(rental.getCar().getId());
        car.setInventory(car.getInventory() + 1);
        carService.update(car);
        return responseDtoMapper.mapToDto(rentalService.save(rental));
    }
}
