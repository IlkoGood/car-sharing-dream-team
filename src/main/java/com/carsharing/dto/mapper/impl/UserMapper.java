package com.carsharing.dto.mapper.impl;

import com.carsharing.dto.mapper.ResponseDtoMapper;
import com.carsharing.dto.response.UserResponseDto;
import com.carsharing.model.User;
import org.springframework.stereotype.Component;

@Component
public class UserMapper implements ResponseDtoMapper<UserResponseDto, User> {
    public UserResponseDto mapToDto(User user) {
        UserResponseDto responseDto = new UserResponseDto();
        responseDto.setId(user.getId());
        responseDto.setEmail(user.getEmail());
        responseDto.setFirstName(user.getFirstName());
        responseDto.setLastName(user.getLastName());
        responseDto.setRole(user.getRole().name());
        return responseDto;
    }
}
