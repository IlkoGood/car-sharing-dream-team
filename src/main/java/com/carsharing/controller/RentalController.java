package com.carsharing.controller;

import com.carsharing.dto.mapper.RequestDtoMapper;
import com.carsharing.dto.mapper.ResponseDtoMapper;
import com.carsharing.dto.request.RentalRequestDto;
import com.carsharing.dto.response.RentalResponseDto;
import com.carsharing.exception.DataProcessingException;
import com.carsharing.model.Rental;
import com.carsharing.security.AccessService;
import com.carsharing.service.RentalService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
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
    @Operation(summary = "Add rental", description = "Create a new rental record")
    public RentalResponseDto create(@Parameter(schema = @Schema(type = "String",
            defaultValue = """
                    {
                        "rentalDate":"2023-05-29T00:00:00.000Z",\s
                        "returnDate":"2023-05-30T00:00:00.000Z",\s
                        "carId":1,\s
                        "userId":1\s
                    }"""))@RequestBody RentalRequestDto requestDto,
                                    Authentication authentication) {
        Rental rental = requestDtoMapper.mapToModel(requestDto);
        if (accessService.checkUserAccess(authentication, rental.getUserId())) {
            throw new RuntimeException("You do not have access to this data");
        }
        rentalService.createRental(rental);
        return responseDtoMapper.mapToDto(rentalService.save(rental));
    }

    @GetMapping
    @Operation(summary = "Get rentals",
            description = "Get rentals based on user ID and activity status")
    public List<RentalResponseDto> get(Authentication authentication,
                                       @Parameter(description = "User id", example = "1")
                                       @RequestParam(required = false) Long userId,
                                       @Parameter(description = "Flag indicating the activity status",
                                               example = "true")
                                       @RequestParam(required = false) Boolean isActive) {
        if (accessService.checkUserAccess(authentication, userId)) {
            throw new RuntimeException("You do not have access to this data");
        }
        return rentalService.getByParam(userId, isActive).stream()
                .map(responseDtoMapper::mapToDto)
                .collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get rental by id", description = "Retrieve rental details by id")
    public RentalResponseDto getById(Authentication authentication,
                                     @Parameter(description = "Rental id", example = "1")
                                     @PathVariable Long id) {
        Rental rental = rentalService.getById(id);
        if (accessService.checkUserAccess(authentication, rental.getUserId())) {
            throw new RuntimeException("You do not have access to this data");
        }
        return responseDtoMapper.mapToDto(rental);
    }

    @PutMapping("/{id}/return")
    @Operation(summary = "Set actual return date ",
            description = "Record actual return date for a rental")
    public RentalResponseDto close(Authentication authentication,
                                   @Parameter(description = "Rental id", example = "1")
                                   @PathVariable Long id) {
        Rental rental = rentalService.getById(id);
        if (accessService.checkUserAccess(authentication, rental.getUserId())) {
            throw new RuntimeException("You do not have access to this data");
        }
        return responseDtoMapper.mapToDto(rentalService.save(rentalService.closeRental(rental)));
    }
}
