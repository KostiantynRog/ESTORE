package com.rog.EShop.services;

import com.rog.EShop.dto.UserRegisterDto;
import org.keycloak.representations.AccessTokenResponse;
import org.keycloak.representations.idm.UserRepresentation;

import java.util.List;

public interface KeycloakService {
     AccessTokenResponse getToken();
     UserRepresentation getUserByUsername(String userName);
     List<String> getUserRoles(String userId);
     UserRepresentation createUser(UserRegisterDto userRegisterDto);
}
