package com.carsharing.controller;

import com.carsharing.dto.mapper.RequestDtoMapper;
import com.carsharing.dto.mapper.ResponseDtoMapper;
import com.carsharing.dto.request.RentalRequestDto;
import com.carsharing.dto.response.RentalResponseDto;
import com.carsharing.model.Car;
import com.carsharing.model.Rental;
import com.carsharing.service.CarService;
import com.carsharing.service.RentalService;
import com.carsharing.service.UserService;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
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
    private static final String MANAGER = "MANAGER";
    private final RentalService rentalService;
    private final CarService carService;
    private final UserService userService;
    private final RequestDtoMapper<RentalRequestDto, Rental> requestDtoMapper;
    private final ResponseDtoMapper<RentalResponseDto, Rental> responseDtoMapper;

    @PostMapping
    @PreAuthorize("hasAnyAuthority('CUSTOMER', 'MANAGER')")
    public RentalResponseDto createRental(Authentication authentication,
                                          @RequestBody RentalRequestDto requestDto) {
        boolean isManager = authentication.getAuthorities().stream()
                .anyMatch(role -> role.getAuthority().equals(MANAGER));
        Long authUserId = userService.findByEmail(authentication.getName()).getId();
        if (isManager || Objects.equals(authUserId, requestDto.getUserId())) {
            Car car = carService.getById(requestDto.getCarId());
            if (car.getInventory() < 1) {
                throw new RuntimeException("You don`t have cars for rental [" + car + ']');
            }
            car.setInventory(car.getInventory() - 1);
            carService.update(car);
            return responseDtoMapper
                    .mapToDto(rentalService.save(requestDtoMapper.mapToModel(requestDto)));
        }
        throw new RuntimeException("You do not have access to create rental with user id: "
                + requestDto.getUserId());
    }

    @GetMapping
    @PreAuthorize("hasAnyAuthority('CUSTOMER', 'MANAGER')")
    public List<RentalResponseDto> getRentals(Authentication authentication,
                                              @RequestParam(required = false) Long userId,
                                              @RequestParam(required = false) Boolean isActive) {
        boolean isManager = authentication.getAuthorities().stream()
                .anyMatch(role -> role.getAuthority().equals(MANAGER));
        Long authUserId = userService.findByEmail(authentication.getName()).getId();
        if (!isManager) {
            if (userId != null && !Objects.equals(authUserId, userId)) {
                throw new RuntimeException("You do not have access to user data with id: "
                        + userId);
            } else {
                userId = authUserId;
            }
        }
        List<Rental> rentals = userId != null ? rentalService.getAllByUserId(userId)
                : rentalService.getAll();
        if (isActive != null) {
            rentals = rentals.stream()
                    .filter(r -> (r.getActualReturnDate() == null) == isActive)
                    .collect(Collectors.toList());
        }
        return rentals.stream()
                .map(responseDtoMapper::mapToDto)
                .collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('CUSTOMER', 'MANAGER')")
    public RentalResponseDto getById(Authentication authentication, @PathVariable Long id) {
        boolean isManager = authentication.getAuthorities().stream()
                .anyMatch(role -> role.getAuthority().equals(MANAGER));
        Long authUserId = userService.findByEmail(authentication.getName()).getId();
        Rental rental = rentalService.getById(id);
        if (isManager || Objects.equals(authUserId, rental.getUser().getId())) {
            return responseDtoMapper.mapToDto(rental);
        }
        throw new RuntimeException("You do not have access to rental data with id: " + id);
    }

    @PutMapping("/{id}/return")
    @PreAuthorize("hasAnyAuthority('CUSTOMER', 'MANAGER')")
    public RentalResponseDto closeRental(Authentication authentication, @PathVariable Long id) {
        boolean isManager = authentication.getAuthorities().stream()
                .anyMatch(role -> role.getAuthority().equals(MANAGER));
        Long authUserId = userService.findByEmail(authentication.getName()).getId();
        Rental rental = rentalService.getById(id);
        if (isManager || Objects.equals(authUserId, rental.getUser().getId())) {
            if (rental.getActualReturnDate() != null) {
                throw new RuntimeException("Rental with id: '" + id + "' is no longer active!");
            }
            rental.setActualReturnDate(LocalDateTime.now());
            Car car = carService.getById(rental.getCar().getId());
            car.setInventory(car.getInventory() + 1);
            carService.update(car);
            return responseDtoMapper.mapToDto(rentalService.save(rental));
        }
        throw new RuntimeException("You do not have access to rental data with id: " + id);
    }
}
