package com.carsharing.dto.mapper;

public interface RequestDtoMapper<D, T> {
    T mapToModel(D dto);
}
