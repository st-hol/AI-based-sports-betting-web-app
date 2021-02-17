package com.sportbetapp.controller.mobile.rest.api.mapper;

import com.sportbetapp.controller.mobile.rest.api.dto.MobileUserDto;
import com.sportbetapp.domain.user.User;
import com.sportbetapp.dto.user.UserDto;

public final class UserMapper {
    private UserMapper() {
    }

    public static MobileUserDto toMobileDto(User user) {
        if (user == null) {
            return null;
        }

        MobileUserDto dto = new MobileUserDto();
        dto.setId(user.getId());
        dto.setName(user.getName());
        dto.setEmail(user.getEmail());
        dto.setPassword(user.getPassword());
        dto.setBirth(user.getBirth());
        dto.setBalance(user.getBalance());

        return dto;
    }

    public static UserDto toUserDto(MobileUserDto user) {
        if (user == null) {
            return null;
        }
        UserDto dto = new UserDto();
        dto.setId(user.getId());
        dto.setName(user.getName());
        dto.setEmail(user.getEmail());
        dto.setPassword(user.getPassword());
        dto.setBirth(user.getBirth());
        dto.setBalance(user.getBalance());

        return dto;
    }

}
