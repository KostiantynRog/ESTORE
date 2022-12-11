package com.rog.EShop.services;

import com.rog.EShop.dto.*;
import com.rog.EShop.entity.Role;
import com.rog.EShop.entity.User;
import com.rog.EShop.exceptions.BadRequestException;
import com.rog.EShop.exceptions.ConflictException;
import com.rog.EShop.exceptions.NotFoundException;
import com.rog.EShop.mapper.UserMapper;
import com.rog.EShop.properties.KeycloakProperties;
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
    private final KeycloakProperties keycloakProperties;


    public UserService(UserRepository userRepository, UserMapper userMapper,
                       KeycloakProperties keycloakProperties) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
        this.keycloakProperties = keycloakProperties;
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

        ResponseTokenDto responseTokenDto = getTokenFromKeycloak();
        ResponseEntity<String> response = createUserInKeycloak(userRegisterDto, responseTokenDto.getAccessToken());
        if (response.getStatusCode().isError()) {
            throw new RuntimeException("Error " + response.getStatusCode());
        }

        ResponseEntity<List<ResponseKeycloakDto>> responseKeycloakDto = getUserFromKeycloakByUsername(user.getUsername(),
                responseTokenDto.getAccessToken());

        List<ResponseKeycloakDto> responseKeycloakDto1 = responseKeycloakDto.getBody();
        user.setKeycloakId(responseKeycloakDto1.get(0).getId());
        Long timeStamp = responseKeycloakDto1.get(0).getCreatedTimestamp();
        user.setRegisterDate(LocalDateTime.ofInstant(Instant.ofEpochMilli(timeStamp), ZoneOffset.UTC));
        ResponseEntity<List<ResponseUserRoleDto>> responseUserRoleDto = getUserRole(user.getKeycloakId().toString(),
                keycloakProperties.getRealm());
        List<ResponseUserRoleDto> responseUserRoleDto1 = responseUserRoleDto.getBody();
//        String role = responseUserRoleDto1.get(0).getName();
//        user.setRoles();

        userRepository.save(user);
        return userMapper.toDTO(user);

    }

    private ResponseEntity<List<ResponseKeycloakDto>> getUserFromKeycloakByUsername(String username,
                                                                                    String accessToken) {
        RestTemplate restTemplate = new RestTemplate();
        String url = keycloakProperties.getHost() + "/admin/realms/ESTORE/users?username={username}&exact=true";
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(accessToken);
        Map<String, String> params = new HashMap<>();
        params.put("username", username);
        HttpEntity request = new HttpEntity(headers);
        ResponseEntity<List<ResponseKeycloakDto>> responseKeycloakDto = restTemplate
                .exchange(url, HttpMethod.GET, request,
                        new ParameterizedTypeReference<>() {
                        }, params);
        return responseKeycloakDto;
    }

    private ResponseEntity<String> createUserInKeycloak(UserRegisterDto userRegisterDto,
                                                        String accessToken) {
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(accessToken);
        KeycloakUserDto keycloakUserDto = new KeycloakUserDto();
        keycloakUserDto.setFirstName(userRegisterDto.getFirstName());
        keycloakUserDto.setLastName(userRegisterDto.getLastName());
        keycloakUserDto.setUsername(userRegisterDto.getUsername());
        keycloakUserDto.setEmail(userRegisterDto.getEmail());
        keycloakUserDto.setEmailVerified(true);
        keycloakUserDto.setEnabled(true);
        keycloakUserDto.setRealmRoles(List.of(Role.ROLE_USER));
        Credential credential = new Credential();
        credential.setTemporary(false);
        credential.setType("password");
        credential.setValue(userRegisterDto.getPassword());
        keycloakUserDto.setCredentials(List.of(credential));
        HttpEntity<KeycloakUserDto> keycloakRequest = new HttpEntity<>(keycloakUserDto, headers);
        ResponseEntity<String> response = restTemplate
                .postForEntity(keycloakProperties.getHost() + "/admin/realms/ESTORE/users", keycloakRequest,
                        String.class);
        return response;
    }



    private  ResponseTokenDto getTokenFromKeycloak() {
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        MultiValueMap<String, String> formData = new LinkedMultiValueMap<>();
        formData.add("grant_type", "password");
        formData.add("client_id", keycloakProperties.getClientId());
        formData.add("username", "admin");
        formData.add("password", "admin");
        formData.add("client_secret", keycloakProperties.getClientSecret());
        HttpEntity<MultiValueMap<String, String>> tokenRequest = new HttpEntity<>(formData, headers);
        ResponseTokenDto responseTokenDto = restTemplate
                .postForObject( keycloakProperties.getHost() +
                                "/realms/ESTORE/protocol/openid-connect/token",
                        tokenRequest, ResponseTokenDto.class);
        return responseTokenDto;
    }
    private ResponseEntity <List<ResponseUserRoleDto>> getUserRole(String userId, String realm){
        RestTemplate restTemplate = new RestTemplate();
        String url =  keycloakProperties.getHost() +
                "/admin/realms/ESTORE/{realm}/users/{id}/role-mappings";
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.add(HttpHeaders.AUTHORIZATION, "Bearer " + getTokenFromKeycloak());
        Map<String, String> params = new HashMap<>();
        params.put("id", userId);
        params.put("realm", realm);
        HttpEntity request = new HttpEntity(headers);
        ResponseEntity <List<ResponseUserRoleDto>> responseUserRoleDto = restTemplate
                .exchange(url, HttpMethod.GET, request,
                        new ParameterizedTypeReference<>() {
                        }, params);
        return responseUserRoleDto;
    }

}
