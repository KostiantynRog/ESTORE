package com.rog.EShop.services;

import com.rog.EShop.dto.UserDto;
import com.rog.EShop.dto.UserRegisterDto;
import com.rog.EShop.dto.keycloak.UserRepresentation;
import com.rog.EShop.entity.User;
import com.rog.EShop.exceptions.BadRequestException;
import com.rog.EShop.exceptions.ConflictException;
import com.rog.EShop.exceptions.NotFoundException;
import com.rog.EShop.mapper.UserMapper;
import com.rog.EShop.repository.UserRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Objects;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final KeycloakService keycloakService;


    public UserService(UserRepository userRepository, UserMapper userMapper, KeycloakService keycloakService) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
        this.keycloakService = keycloakService;
    }


    public UserDto findById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Not found"));

        UserDto userDto = userMapper.toDTO(user);
        String accessToken = keycloakService.getAccessToken();
        if (user.getKeycloakId() != null) {
            userDto.setRoles(keycloakService.getUserRoles(user.getKeycloakId().toString(), accessToken));
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

        String accessToken = keycloakService.getAccessToken();
        ResponseEntity<String> response = keycloakService.createUser(userRegisterDto, accessToken);
        if (response.getStatusCode().isError()) {
            throw new RuntimeException("Error " + response.getStatusCode());
        }

        UserRepresentation userRepresentation = keycloakService.getUserByUsername(user.getUsername(), accessToken)
                .getBody().get(0);
        user.setKeycloakId(userRepresentation.getId());
        user.setRegisterDate(LocalDateTime.ofInstant(Instant.ofEpochMilli(userRepresentation.getCreatedTimestamp()),
                ZoneOffset.UTC));

        userRepository.save(user);
        UserDto userDto = userMapper.toDTO(user);
        userDto.setRoles(keycloakService.getUserRoles(user.getKeycloakId().toString(),
                accessToken));
        return userDto;

    }
}
