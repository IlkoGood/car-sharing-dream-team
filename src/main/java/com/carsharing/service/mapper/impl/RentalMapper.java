package com.carsharing.service.mapper.impl;

import com.carsharing.service.mapper.RequestDtoMapper;
import com.carsharing.service.mapper.ResponseDtoMapper;
import com.carsharing.dto.request.RentalRequestDto;
import com.carsharing.dto.response.RentalResponseDto;
import com.carsharing.model.Rental;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

@AllArgsConstructor
@Component
public class RentalMapper implements RequestDtoMapper<RentalRequestDto, Rental>,
        ResponseDtoMapper<RentalResponseDto, Rental> {

    @Override
    public Rental mapToModel(RentalRequestDto dto) {
        Rental rental = new Rental();
        rental.setRentalDate(LocalDateTime.parse(dto.getRentalDate()));
        rental.setReturnDate(LocalDateTime.parse(dto.getReturnDate()));
        rental.setActualReturnDate(dto.getActualReturnDate());
        rental.setUserId(Long.valueOf(dto.getUserId()));
        rental.setCarId(Long.valueOf(dto.getCarId()));
        return rental;
    }

    @Override
    public RentalResponseDto mapToDto(Rental rental) {
        RentalResponseDto rentalResponseDto = new RentalResponseDto();
        rentalResponseDto.setId(rental.getId());
        rentalResponseDto.setRentalDate(rental.getRentalDate());
        rentalResponseDto.setReturnDate(rental.getReturnDate());
        rentalResponseDto.setActualReturnDate(rental.getActualReturnDate());
        rentalResponseDto.setUserId(rental.getUserId());
        rentalResponseDto.setCarId(rental.getCarId());
        return rentalResponseDto;
    }
}
