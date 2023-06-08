package com.carsharing.controller;

import com.carsharing.dto.mapper.RequestDtoMapper;
import com.carsharing.dto.mapper.ResponseDtoMapper;
import com.carsharing.dto.request.RentalRequestDto;
import com.carsharing.dto.response.RentalResponseDto;
import com.carsharing.model.Rental;
import com.carsharing.security.AccessService;
import com.carsharing.service.RentalService;
import java.util.List;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
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
    private final RentalService rentalService;
    private final AccessService accessService;
    private final RequestDtoMapper<RentalRequestDto, Rental> requestDtoMapper;
    private final ResponseDtoMapper<RentalResponseDto, Rental> responseDtoMapper;

    @PostMapping
    public RentalResponseDto create(Authentication authentication,
                                    @RequestBody RentalRequestDto requestDto) {
        Rental rental = requestDtoMapper.mapToModel(requestDto);
        if (accessService.checkUserAccess(authentication, rental.getUserId())) {
            throw new RuntimeException("You do not have access to this data");
        }
        rentalService.createRental(rental);
        return responseDtoMapper.mapToDto(rentalService.save(rental));
    }

    @GetMapping
    public List<RentalResponseDto> get(Authentication authentication,
                                       @RequestParam(required = false) Long userId,
                                       @RequestParam(required = false) Boolean isActive) {
        if (accessService.checkUserAccess(authentication, userId)) {
            throw new RuntimeException("You do not have access to this data");
        }
        return rentalService.getByParam(userId, isActive).stream()
                .map(responseDtoMapper::mapToDto)
                .collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public RentalResponseDto getById(Authentication authentication, @PathVariable Long id) {
        Rental rental = rentalService.getById(id);
        if (accessService.checkUserAccess(authentication, rental.getUserId())) {
            throw new RuntimeException("You do not have access to this data");
        }
        return responseDtoMapper.mapToDto(rental);
    }

    @PutMapping("/{id}/return")
    public RentalResponseDto close(Authentication authentication, @PathVariable Long id) {
        Rental rental = rentalService.getById(id);
        if (accessService.checkUserAccess(authentication, rental.getUserId())) {
            throw new RuntimeException("You do not have access to this data");
        }
        return responseDtoMapper.mapToDto(rentalService.save(rentalService.closeRental(rental)));
    }
}
