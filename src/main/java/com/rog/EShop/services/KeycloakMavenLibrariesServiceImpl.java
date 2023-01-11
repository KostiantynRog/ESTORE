package com.rog.EShop.services;

import com.rog.EShop.dto.UserRegisterDto;
import com.rog.EShop.exceptions.NotFoundException;
import com.rog.EShop.properties.ApplicationProperties;
import org.apache.http.impl.client.HttpClientBuilder;
import org.keycloak.authorization.client.AuthzClient;
import org.keycloak.authorization.client.Configuration;
import org.keycloak.representations.AccessTokenResponse;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;

@Component
public class KeycloakMavenLibrariesServiceImpl implements KeycloakService {
    private final ApplicationProperties applicationProperties;
    private final KeycloakClientFactory keycloakClientFactory;

    public KeycloakMavenLibrariesServiceImpl(ApplicationProperties applicationProperties,
                                             KeycloakClientFactory keycloakClientFactory) {
        this.applicationProperties = applicationProperties;
        this.keycloakClientFactory = keycloakClientFactory;
    }
    @Override
    public AccessTokenResponse getToken() {
        Configuration authzConf = new Configuration(
                this.applicationProperties.getKeycloak().getBaseUrl(),
                this.applicationProperties.getKeycloak().getRealm(),
                this.applicationProperties.getKeycloak().getClientId(),
                Collections.singletonMap("secret", this.applicationProperties.getKeycloak().getClientSecret()),
                HttpClientBuilder.create().build()
        );
        AuthzClient authzClient = AuthzClient.create(authzConf);
        return authzClient.obtainAccessToken();
    }
    @Override
    public UserRepresentation getUserByUsername(String userName) {
        List<UserRepresentation> userRepresentations = keycloakClientFactory.getInstance()
                .realm(applicationProperties.getKeycloak().getRealm())
                .users()
                .searchByUsername(userName, true);
        if(userRepresentations.isEmpty()){
            throw new NotFoundException("user does not exist " + userName);
        }
       return userRepresentations.get(0);
    }

    @Override
    public List<String> getUserRoles(String userId) {
        return null;
    }

    @Override
    public UserRepresentation createUser(UserRegisterDto userRegisterDto) {
        return null;
    }

    public UserRepresentation getUserByUuid(String userUuid) {
        UserRepresentation userRepresentation = keycloakClientFactory.getInstance()
                .realm(applicationProperties.getKeycloak().getRealm())
                .users()
                .get(userUuid)
                .toRepresentation();
        return userRepresentation;
    }
}
