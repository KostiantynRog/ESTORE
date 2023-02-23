package com.rog.EShop.services;

import com.rog.EShop.dto.UserRegisterDto;

import com.rog.EShop.exceptions.NotFoundException;
import com.rog.EShop.properties.ApplicationProperties;
import org.keycloak.representations.AccessTokenResponse;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.MappingsRepresentation;
import org.keycloak.representations.idm.RoleRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Transactional
public class KeycloakServiceImpl implements KeycloakService {
    private final ApplicationProperties applicationProperties;
    private AccessTokenResponse token;
    private LocalDateTime tokenExpiryDate;


    public KeycloakServiceImpl(ApplicationProperties applicationProperties) {
        this.applicationProperties = applicationProperties;
    }

    public String getAccessToken() {
        if (isTokenExpired()) {
            token = getToken();
            tokenExpiryDate = LocalDateTime.now().plusSeconds(token.getExpiresIn()).minusSeconds(5);
        }
        return token.getToken();
    }

    private boolean isTokenExpired() {
        if (token == null || tokenExpiryDate.isBefore(LocalDateTime.now())) {
            return true;
        }
        return false;
    }
    @Override
    public AccessTokenResponse getToken() {
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        MultiValueMap<String, String> formData = new LinkedMultiValueMap<>();
        formData.add("grant_type", "client_credentials");
        formData.add("client_id", applicationProperties.getKeycloak().getClientId());
        formData.add("client_secret", applicationProperties.getKeycloak().getClientSecret());
        HttpEntity<MultiValueMap<String, String>> tokenRequest = new HttpEntity<>(formData, headers);
        AccessTokenResponse accessTokenResponse = restTemplate
                .postForObject(applicationProperties.getKeycloak().getBaseUrl() +
                                "/realms/" + applicationProperties.getKeycloak().getRealm()
                                + "/protocol/openid-connect/token",
                                  tokenRequest, AccessTokenResponse.class);
        return accessTokenResponse;
    }


    @Override
    public UserRepresentation createUser(UserRegisterDto userRegisterDto) {
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(getAccessToken());
        UserRepresentation userRepresentation = new UserRepresentation();
        userRepresentation.setFirstName(userRegisterDto.getFirstName());
        userRepresentation.setLastName(userRegisterDto.getLastName());
        userRepresentation.setUsername(userRegisterDto.getUsername());
        userRepresentation.setEmail(userRegisterDto.getEmail());
        userRepresentation.setEmailVerified(true);
        userRepresentation.setEnabled(true);
        userRepresentation.setRealmRoles(List.of("ROLE_USER"));
        CredentialRepresentation credential = new CredentialRepresentation();
        credential.setTemporary(false);
        credential.setType("password");
        credential.setValue(userRegisterDto.getPassword());
        userRepresentation.setCredentials(List.of(credential));
        HttpEntity<UserRepresentation> keycloakRequest = new HttpEntity<>(userRepresentation, headers);
        ResponseEntity<String> response = restTemplate
                .postForEntity(applicationProperties.getKeycloak().getBaseUrl() + "/admin/realms/"
                        + applicationProperties.getKeycloak().getRealm()
                        + "/users", keycloakRequest, String.class);
        if (response.getStatusCode().isError()) {
            throw new RuntimeException("Error " + response.getStatusCode());
        }
        UserRepresentation userRepresentation2 =getUserByUsername(userRegisterDto.getUsername());

        return userRepresentation2;
    }

    @Override
    public UserRepresentation getUserById(String id) {
        throw new UnsupportedOperationException("Not implemented");
    }

    @Override
    public UserRepresentation getUserByUsername(String username) {
        RestTemplate restTemplate = new RestTemplate();
        String url = applicationProperties.getKeycloak().getBaseUrl()
                + "/admin/realms/" + applicationProperties.getKeycloak().getRealm()
                + "/users?username={username}&exact=true";
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(getAccessToken());
        Map<String, String> uriVariables = new HashMap<>();
        uriVariables.put("username", username);
        HttpEntity<?> request = new HttpEntity<>(headers);
        ResponseEntity<List<UserRepresentation>> userRepresentation = restTemplate
                .exchange(url, HttpMethod.GET, request,
                        new ParameterizedTypeReference<>() {
                        }, uriVariables);
        if(userRepresentation.getBody().isEmpty()){
            throw new NotFoundException("user does not exist " + username);
        }
        return userRepresentation.getBody().get(0);
    }
    @Override
    public List<String> getUserRoles(String userId) {
        RestTemplate restTemplate = new RestTemplate();
        String url = applicationProperties.getKeycloak().getBaseUrl() +
                "/admin/realms/{realm}/users/{id}/role-mappings";
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(getAccessToken());
        Map<String, String> uriVariables = new HashMap<>();
        uriVariables.put("id", userId);
        uriVariables.put("realm", applicationProperties.getKeycloak().getRealm());
        HttpEntity<?> request = new HttpEntity<>(headers);
        ResponseEntity<MappingsRepresentation> mappingsRepresentation = restTemplate
                .exchange(url, HttpMethod.GET, request,
                        MappingsRepresentation.class, uriVariables);

        return mappingsRepresentation.getBody()
                .getRealmMappings()
                .stream()
                .map(RoleRepresentation::getName).toList();
    }
}
