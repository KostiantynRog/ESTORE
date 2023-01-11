package com.rog.EShop.services;

import com.rog.EShop.dto.UserDto;
import com.rog.EShop.dto.UserRegisterDto;
import com.rog.EShop.entity.User;
import com.rog.EShop.exceptions.BadRequestException;
import com.rog.EShop.exceptions.ConflictException;
import com.rog.EShop.exceptions.NotFoundException;
import com.rog.EShop.mapper.UserMapper;
import com.rog.EShop.repository.UserRepository;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final KeycloakService keycloakService;


    public UserService(UserRepository userRepository, UserMapper userMapper, KeycloakMavenLibrariesServiceImpl keycloakService) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
        this.keycloakService = keycloakService;

    }


    public UserDto findById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Not found"));

        UserDto userDto = userMapper.toDTO(user);

        if (user.getKeycloakId() != null) {
            userDto.setRoles(keycloakService.getUserRoles(user.getKeycloakId().toString()));
        }
        return userDto;
    }

    public UserDto save(UserRegisterDto userRegisterDto) {

        User user = userMapper.toEntity(userRegisterDto);
        if (userRepository.existsUserByUsername(user.getUsername())) {
            throw new ConflictException("This username already exists!");
        }
        if (!Objects.equals(userRegisterDto.getPassword(), userRegisterDto.getConfirmPassword())) {
            throw new BadRequestException("Password does not match!");
        }

        UserRepresentation userRepresentation = keycloakService.createUser(userRegisterDto);
        UserDto userDto = userMapper.toDTO(userRepresentation);
        userDto.setRoles(keycloakService.getUserRoles(user.getKeycloakId().toString()));
        return userDto;

    }
}
