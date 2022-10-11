package com.rog.EShop.mapper;

import com.rog.EShop.dto.UserDto;
import com.rog.EShop.dto.UserRegisterDto;
import com.rog.EShop.entity.Role;
import com.rog.EShop.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;
import java.util.stream.Collectors;

@Mapper
public interface UserMapper {
    @Mapping(target = "authorities", source = "roles")
    UserDto toDTO(User user);
    User toEntity(UserRegisterDto userRegisterDto);

    default List<String> rolesToStrings(List<Role> roles){
        return roles.stream()
                .map(role -> role.getName())
                .collect(Collectors.toList());
    }
}
