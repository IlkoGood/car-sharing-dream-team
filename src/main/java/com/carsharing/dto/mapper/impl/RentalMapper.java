package com.carsharing.dto.mapper.impl;

import com.carsharing.dto.mapper.RequestDtoMapper;
import com.carsharing.dto.mapper.ResponseDtoMapper;
import com.carsharing.dto.request.RentalRequestDto;
import com.carsharing.dto.response.RentalResponseDto;
import com.carsharing.model.Rental;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

@AllArgsConstructor
@Component
public class RentalMapper implements RequestDtoMapper<RentalRequestDto, Rental>,
        ResponseDtoMapper<RentalResponseDto, Rental> {

    @Override
    public Rental mapToModel(RentalRequestDto dto) {
        Rental rental = new Rental();
        rental.setRentalDate(dto.getRentalDate());
        rental.setReturnDate(dto.getReturnDate());
        rental.setActualReturnDate(dto.getActualReturnDate());
        rental.setUserId(dto.getUserId());
        rental.setCarId(dto.getCarId());
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
