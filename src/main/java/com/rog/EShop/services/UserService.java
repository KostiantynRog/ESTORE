package com.rog.EShop.services;

import com.rog.EShop.dto.*;
import com.rog.EShop.entity.Role;
import com.rog.EShop.entity.User;
import com.rog.EShop.exceptions.BadRequestException;
import com.rog.EShop.exceptions.ConflictException;
import com.rog.EShop.exceptions.NotFoundException;
import com.rog.EShop.mapper.UserMapper;
import com.rog.EShop.repository.UserRepository;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;


    public UserService(UserRepository userRepository, UserMapper userMapper) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;

    }

    public UserDto findById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Not found"));

        return userMapper.toDTO(user);
    }


    public UserDto save(UserRegisterDto userRegisterDto) {

        User user = userMapper.toEntity(userRegisterDto);
        if (userRepository.existsUserByUsername(user.getUsername())) {
            throw new ConflictException("This username already exists!");
        }
        if (!Objects.equals(userRegisterDto.getPassword(), userRegisterDto.getConfirmPassword())) {
            throw new BadRequestException("Password does not match!");
        }

        ResponseTokenDto responseTokenDto = getResponseTokenDto();
        ResponseEntity<String> response = getStringResponseEntity(userRegisterDto, responseTokenDto);
        if (response.getStatusCode().isError()) {
            throw new RuntimeException("Error " + response.getStatusCode());
        }

        ResponseEntity<List<ResponseKeycloakDto>> responseKeycloakDto = getListResponseEntity(userRegisterDto, responseTokenDto);

        List<ResponseKeycloakDto> responseKeycloakDto1 = responseKeycloakDto.getBody();
        user.setKeycloakId(responseKeycloakDto1.get(0).getId());
        Long timeStamp = responseKeycloakDto1.get(0).getCreatedTimestamp();
        Instant instant = Instant.ofEpochMilli(timeStamp);
        LocalDateTime localDate = LocalDateTime.ofInstant(instant, ZoneOffset.UTC);
        user.setRegisterDate(localDate);
        User user2 = userRepository.save(user);
        return userMapper.toDTO(user2);
     
    }

    private static ResponseEntity<List<ResponseKeycloakDto>> getListResponseEntity(UserRegisterDto userRegisterDto, ResponseTokenDto responseTokenDto) {
        RestTemplate restTemplate2 = new RestTemplate();
        String url = "http://localhost:8080/admin/realms/ESTORE/users?username={username}&exact=true";
        HttpHeaders headers2 = new HttpHeaders();
        headers2.setBearerAuth(responseTokenDto.getAccessToken());
        Map<String, String> params = new HashMap<>();
        params.put("username", userRegisterDto.getUsername());
        HttpEntity request = new HttpEntity(headers2);
        ResponseEntity<List<ResponseKeycloakDto>> responseKeycloakDto = restTemplate2
                .exchange(url, HttpMethod.GET, request,
                        new ParameterizedTypeReference<List<ResponseKeycloakDto>>() {
                        }, params);
        return responseKeycloakDto;
    }

    private static ResponseEntity<String> getStringResponseEntity(UserRegisterDto userRegisterDto, ResponseTokenDto responseTokenDto) {
        RestTemplate restTemplate1 = new RestTemplate();
        HttpHeaders headers1 = new HttpHeaders();
        headers1.setContentType(MediaType.APPLICATION_JSON);
        headers1.setBearerAuth(responseTokenDto.getAccessToken());
        KeycloakUserDto keycloakUserDto = new KeycloakUserDto();
        keycloakUserDto.setFirstName(userRegisterDto.getFirstName());
        keycloakUserDto.setLastName(userRegisterDto.getLastName());
        keycloakUserDto.setUsername(userRegisterDto.getUsername());
        keycloakUserDto.setEmail("user9@net.com");
        keycloakUserDto.setEmailVerified(true);
        keycloakUserDto.setEnabled(true);
        keycloakUserDto.setRealmRoles(List.of(Role.ROLE_USER));
        Credential credential = new Credential();
        credential.setTemporary(false);
        credential.setType("password");
        credential.setValue("user9");
        keycloakUserDto.setCredentials(List.of(credential));
        HttpEntity<KeycloakUserDto> keycloakRequest = new HttpEntity<>(keycloakUserDto, headers1);
        ResponseEntity<String> response = restTemplate1
                .postForEntity("http://localhost:8080/admin/realms/ESTORE/users", keycloakRequest,
                        String.class);
        return response;
    }

    private static ResponseTokenDto getResponseTokenDto() {
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        MultiValueMap<String, String> formData = new LinkedMultiValueMap<>();
        formData.add("grant_type", "password");
        formData.add("client_id", "estore-api");
        formData.add("username", "admin");
        formData.add("password", "admin");
        formData.add("client_secret", "2iwoTq3NgMMj0CRHIjzWbZ7GRLj5mLEk");
        HttpEntity<MultiValueMap<String, String>> tokenRequest = new HttpEntity<>(formData, headers);
        ResponseTokenDto responseTokenDto = restTemplate
                .postForObject("http://localhost:8080/realms/ESTORE/protocol/openid-connect/token",
                        tokenRequest, ResponseTokenDto.class);
        return responseTokenDto;
    }

}
