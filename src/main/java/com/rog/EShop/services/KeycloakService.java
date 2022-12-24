package com.rog.EShop.services;

import com.rog.EShop.dto.Credential;
import com.rog.EShop.dto.UserRegisterDto;
import com.rog.EShop.dto.keycloak.AccessTokenResponse;
import com.rog.EShop.dto.keycloak.MappingsRepresentation;
import com.rog.EShop.dto.keycloak.RoleRepresentation;
import com.rog.EShop.dto.keycloak.UserRepresentation;
import com.rog.EShop.properties.KeycloakProperties;
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
public class KeycloakService {
    private final KeycloakProperties keycloakProperties;
    private AccessTokenResponse token;
    private LocalDateTime tokenExpiryDate;


    public KeycloakService(KeycloakProperties keycloakProperties) {
        this.keycloakProperties = keycloakProperties;
    }

    public String getAccessToken() {
        if (isTokenExpired()) {
            token = getToken();
            tokenExpiryDate = LocalDateTime.now().plusSeconds(token.getExpiresIn()).minusSeconds(5);
        }
        return token.getAccessToken();
    }

    private boolean isTokenExpired() {
        if (token == null || tokenExpiryDate.isBefore(LocalDateTime.now())) {
            return true;
        }
        return false;
    }

    private AccessTokenResponse getToken() {
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        MultiValueMap<String, String> formData = new LinkedMultiValueMap<>();
        formData.add("grant_type", "client_credentials");
        formData.add("client_id", keycloakProperties.getClientId());
        formData.add("client_secret", keycloakProperties.getClientSecret());
        HttpEntity<MultiValueMap<String, String>> tokenRequest = new HttpEntity<>(formData, headers);
        AccessTokenResponse accessTokenResponse = restTemplate
                .postForObject(keycloakProperties.getHost() +
                                "/realms/" + keycloakProperties.getRealm() + "/protocol/openid-connect/token",
                        tokenRequest, AccessTokenResponse.class);
        return accessTokenResponse;
    }

    public ResponseEntity<String> createUser(UserRegisterDto userRegisterDto,
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
        userRepresentation.setRealmRoles(List.of("ROLE_USER"));
        Credential credential = new Credential();
        credential.setTemporary(false);
        credential.setType("password");
        credential.setValue(userRegisterDto.getPassword());
        userRepresentation.setCredentials(List.of(credential));
        HttpEntity<UserRepresentation> keycloakRequest = new HttpEntity<>(userRepresentation, headers);
        ResponseEntity<String> response = restTemplate
                .postForEntity(keycloakProperties.getHost() + "/admin/realms/" + keycloakProperties.getRealm()
                        + "/users", keycloakRequest, String.class);
        return response;
    }

    public ResponseEntity<List<UserRepresentation>> getUserByUsername(String username,
                                                                      String accessToken) {
        RestTemplate restTemplate = new RestTemplate();
        String url = keycloakProperties.getHost() +
                "/admin/realms/" + keycloakProperties.getRealm() + "/users?username={username}&exact=true";
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(accessToken);
        Map<String, String> uriVariables = new HashMap<>();
        uriVariables.put("username", username);
        HttpEntity<?> request = new HttpEntity<>(headers);
        ResponseEntity<List<UserRepresentation>> UserRepresentation = restTemplate
                .exchange(url, HttpMethod.GET, request,
                        new ParameterizedTypeReference<>() {
                        }, uriVariables);
        return UserRepresentation;
    }

    public List<String> getUserRoles(String userId, String accessToken) {
        RestTemplate restTemplate = new RestTemplate();
        String url = keycloakProperties.getHost() +
                "/admin/realms/{realm}/users/{id}/role-mappings";
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(accessToken);
        Map<String, String> uriVariables = new HashMap<>();
        uriVariables.put("id", userId);
        uriVariables.put("realm", keycloakProperties.getRealm());
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
