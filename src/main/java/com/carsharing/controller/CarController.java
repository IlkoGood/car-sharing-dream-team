package com.carsharing.controller;

import com.carsharing.service.mapper.RequestDtoMapper;
import com.carsharing.service.mapper.ResponseDtoMapper;
import com.carsharing.dto.request.CarRequestDto;
import com.carsharing.dto.response.CarResponseDto;
import com.carsharing.model.Car;
import com.carsharing.service.CarService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
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
    @PreAuthorize("hasAuthority('MANAGER')")
    @Operation(summary = "Add car", description = "Create a new car entry in the system")
    public CarResponseDto create(@Parameter(schema = @Schema(type = "String",
            defaultValue = "{\n"
                    + "    \"model\":\"Camry\",\n"
                    + "    \"brand\":\"Toyota\",\n"
                    + "    \"type\":\"UNIVERSAL\",\n"
                    + "    \"inventory\":10,\n"
                    + "    \"dailyFee\":200\n"
                    + "}"))@RequestBody CarRequestDto carRequestDto) {
        return responseDtoMapper.mapToDto(carService
                .save(requestDtoMapper.mapToModel(carRequestDto)));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get car by id", description = "Retrieve the car information by its id")
    public CarResponseDto get(@Parameter(description = "Car id", example = "1")
                                  @PathVariable Long id) {
        return responseDtoMapper.mapToDto(carService.getById(id));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('MANAGER')")
    @Operation(summary = "Update car by id",
            description = "Update car information by providing the car id")
    public CarResponseDto update(@Parameter(description = "Car id", example = "1")
                                     @PathVariable Long id,
                                 @Parameter(schema = @Schema(type = "String",
                                         defaultValue = "{\n"
                                                 + "    \"model\":\"Camry\",\n"
                                                 + "    \"brand\":\"Toyota\",\n"
                                                 + "    \"type\":\"UNIVERSAL\",\n"
                                                 + "    \"inventory\":10,\n"
                                                 + "    \"dailyFee\":300\n"
                                                 + "}"))
                                 @RequestBody CarRequestDto requestDto) {
        Car car = requestDtoMapper.mapToModel(requestDto);
        car.setId(id);
        Car updateCar = carService.update(car);
        return responseDtoMapper.mapToDto(updateCar);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('MANAGER')")
    @Operation(summary = "Delete car by id", description = "Delete car by id")
    void delete(@Parameter(description = "Car id", example = "1")@PathVariable Long id) {
        carService.delete(id);
    }

    @GetMapping
    @Operation(summary = "Get all car", description = "List of all car")
    public List<CarResponseDto> getAll() {
        return carService.getAll().stream()
                .map(responseDtoMapper::mapToDto)
                .collect(Collectors.toList());
    }
}
