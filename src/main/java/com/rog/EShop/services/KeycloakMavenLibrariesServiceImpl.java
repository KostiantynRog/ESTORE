package com.rog.EShop.services;

import com.rog.EShop.dto.UserRegisterDto;
import com.rog.EShop.exceptions.BadRequestException;
import com.rog.EShop.exceptions.ConflictException;
import com.rog.EShop.exceptions.NotFoundException;
import com.rog.EShop.properties.ApplicationProperties;
import org.apache.http.impl.client.HttpClientBuilder;
import org.keycloak.admin.client.resource.UsersResource;
import org.keycloak.authorization.client.AuthzClient;
import org.keycloak.authorization.client.Configuration;
import org.keycloak.representations.AccessTokenResponse;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.RoleRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import javax.ws.rs.core.Response;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class KeycloakMavenLibrariesServiceImpl implements KeycloakService {
    private final Logger log = LoggerFactory.getLogger(KeycloakMavenLibrariesServiceImpl.class);
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
        List<UserRepresentation> userRepresentations = getUsersResource()
                .searchByUsername(userName, true);
        if (userRepresentations.isEmpty()) {
            throw new NotFoundException("user does not exist " + userName);
        }
        return userRepresentations.get(0);
    }

    @Override
    public List<String> getUserRoles(String userId) {
        List<RoleRepresentation> roleRepresentations = getUsersResource()
                .get(userId)
                .roles()
                .getAll()
                .getRealmMappings();

        return roleRepresentations.stream()
                .map(RoleRepresentation::toString)
                .collect(Collectors.toList());
    }

    @Override
    public UserRepresentation createUser(UserRegisterDto userRegisterDto) {
        UserRepresentation user = new UserRepresentation();
        user.setFirstName(userRegisterDto.getFirstName());
        user.setLastName(userRegisterDto.getLastName());
        user.setUsername(userRegisterDto.getUsername());
        user.setEmail(userRegisterDto.getEmail());
        user.setEmailVerified(true);
        user.setEnabled(true);
        CredentialRepresentation credential = new CredentialRepresentation();
        credential.setTemporary(false);
        credential.setType("password");
        credential.setValue(userRegisterDto.getPassword());
        user.setCredentials(List.of(credential));
        String path;
        try (Response response = getUsersResource().create(user)) {

            switch (HttpStatus.valueOf(response.getStatus())) {
                case CREATED -> log.info("User successfully created");
                case BAD_REQUEST -> throw new BadRequestException("Bad request " + response.getStatusInfo());
                case CONFLICT -> throw new ConflictException("User already exists " + response.getStatusInfo());
                default -> throw new NotFoundException("User is not created " + response.getStatusInfo());
            }

            path = response.getLocation().getPath();
        }
        String userUuid = path.substring(path.lastIndexOf('/') + 1);
        user.setId(userUuid);
        return user;
    }

    private UsersResource getUsersResource() {
        return keycloakClientFactory.getInstance()
                .realm(applicationProperties.getKeycloak().getRealm())
                .users();
    }

    @Override
    public UserRepresentation getUserById(String id) {
        UserRepresentation userRepresentation = getUsersResource()
                .get(id)
                .toRepresentation();
        return userRepresentation;
    }
}
