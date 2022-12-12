package com.rog.EShop.services;

import com.rog.EShop.dto.Credential;
import com.rog.EShop.dto.UserDto;
import com.rog.EShop.dto.UserRegisterDto;
import com.rog.EShop.dto.keycloak.AccessTokenResponse;
import com.rog.EShop.dto.keycloak.MappingsRepresentation;
import com.rog.EShop.dto.keycloak.UserRepresentation;
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

        AccessTokenResponse accessTokenResponse = getTokenFromKeycloak();
        ResponseEntity<String> response = createUserInKeycloak(userRegisterDto, accessTokenResponse.getAccessToken());
        if (response.getStatusCode().isError()) {
            throw new RuntimeException("Error " + response.getStatusCode());
        }

        ResponseEntity<List<UserRepresentation>> UserRepresentation = getUserFromKeycloakByUsername(user.getUsername(),
                accessTokenResponse.getAccessToken());

        List<UserRepresentation> UserRepresentation1 = UserRepresentation.getBody();
        user.setKeycloakId(UserRepresentation1.get(0).getId());
        Long timeStamp = UserRepresentation1.get(0).getCreatedTimestamp();
        user.setRegisterDate(LocalDateTime.ofInstant(Instant.ofEpochMilli(timeStamp), ZoneOffset.UTC));
        List<String> roles = getUserRoles(user.getKeycloakId().toString(),
                accessTokenResponse.getAccessToken()).getBody().getRealmMappings().
                stream().map(roleRepresentation -> roleRepresentation.getName()).toList();
        userRepository.save(user);
        UserDto userDto = userMapper.toDTO(user);
        userDto.setRoles(roles);
        return userDto;

    }

    private ResponseEntity<List<UserRepresentation>> getUserFromKeycloakByUsername(String username,
                                                                                   String accessToken) {
        RestTemplate restTemplate = new RestTemplate();
        String url = keycloakProperties.getHost() + "/admin/realms/ESTORE/users?username={username}&exact=true";
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(accessToken);
        Map<String, String> params = new HashMap<>();
        params.put("username", username);
        HttpEntity request = new HttpEntity(headers);
        ResponseEntity<List<UserRepresentation>> UserRepresentation = restTemplate
                .exchange(url, HttpMethod.GET, request,
                        new ParameterizedTypeReference<>() {
                        }, params);
        return UserRepresentation;
    }

    private ResponseEntity<String> createUserInKeycloak(UserRegisterDto userRegisterDto,
                                                        String accessToken) {
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(accessToken);
        UserRepresentation userRepresentation = new UserRepresentation();
        userRepresentation.setFirstName(userRegisterDto.getFirstName());
        userRepresentation.setLastName(userRegisterDto.getLastName());
        userRepresentation.setUsername(userRegisterDto.getUsername());
        userRepresentation.setEmail(userRegisterDto.getEmail());
        userRepresentation.setEmailVerified(true);
        userRepresentation.setEnabled(true);
        userRepresentation.setRealmRoles(List.of(Role.ROLE_USER));
        Credential credential = new Credential();
        credential.setTemporary(false);
        credential.setType("password");
        credential.setValue(userRegisterDto.getPassword());
        userRepresentation.setCredentials(List.of(credential));
        HttpEntity<UserRepresentation> keycloakRequest = new HttpEntity<>(userRepresentation, headers);
        ResponseEntity<String> response = restTemplate
                .postForEntity(keycloakProperties.getHost() + "/admin/realms/ESTORE/users", keycloakRequest,
                        String.class);
        return response;
    }

    private AccessTokenResponse getTokenFromKeycloak() {
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
        AccessTokenResponse accessTokenResponse = restTemplate
                .postForObject(keycloakProperties.getHost() +
                                "/realms/ESTORE/protocol/openid-connect/token",
                        tokenRequest, AccessTokenResponse.class);
        return accessTokenResponse;
    }

    private ResponseEntity<MappingsRepresentation> getUserRoles(String userId, String accessToken) {
        RestTemplate restTemplate = new RestTemplate();
        String url = keycloakProperties.getHost() +
                "/admin/realms/{realm}/users/{id}/role-mappings";
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(accessToken);
        Map<String, String> params = new HashMap<>();
        params.put("id", userId);
        params.put("realm", keycloakProperties.getRealm());
        HttpEntity request = new HttpEntity(headers);
        ResponseEntity<MappingsRepresentation> mappingsRepresentation = restTemplate
                .exchange(url, HttpMethod.GET, request,
                        new ParameterizedTypeReference<>() {
                        }, params);
        return mappingsRepresentation;
    }

}
