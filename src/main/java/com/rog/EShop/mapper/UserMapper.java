package com.rog.EShop.mapper;

import com.rog.EShop.dto.UserDto;
import com.rog.EShop.dto.UserRegisterDto;
import com.rog.EShop.entity.User;
import org.mapstruct.Mapper;

@Mapper
public interface UserMapper {
    UserDto toDTO(User user);


    User toEntity(UserRegisterDto userRegisterDto);

}
