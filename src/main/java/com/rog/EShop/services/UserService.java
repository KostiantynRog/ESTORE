package com.rog.EShop.services;

import com.rog.EShop.dto.UserDto;
import com.rog.EShop.dto.UserRegisterDto;
import com.rog.EShop.exceptions.BadRequestException;
import com.rog.EShop.mapper.UserMapper;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
public class UserService {

    private final UserMapper userMapper;
    private final KeycloakService keycloakService;


    public UserService(UserMapper userMapper, KeycloakMavenLibrariesServiceImpl keycloakService) {
        this.userMapper = userMapper;
        this.keycloakService = keycloakService;

    }


    public UserDto findById(String id) {
        UserRepresentation userRepresentation = keycloakService.getUserById(id);
        UserDto userDto = userMapper.toDTO(userRepresentation);
        userDto.setRoles(keycloakService.getUserRoles(id));
        return userDto;
    }

    public UserDto save(UserRegisterDto userRegisterDto) {
        if (!Objects.equals(userRegisterDto.getPassword(), userRegisterDto.getConfirmPassword())) {
            throw new BadRequestException("Password does not match!");
        }

        UserRepresentation userRepresentation = keycloakService.createUser(userRegisterDto);
        UserDto userDto = userMapper.toDTO(userRepresentation);
        userDto.setRoles(keycloakService.getUserRoles(userDto.getId()));
        return userDto;

    }

    public List<String> getUserRoles(String id) {
        return keycloakService.getUserRoles(id);
    }
}
