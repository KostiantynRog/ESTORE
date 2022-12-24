package com.rog.EShop.controllers;

import com.rog.EShop.dto.TokenDto;
import com.rog.EShop.dto.keycloak.AccessTokenResponse;
import com.rog.EShop.dto.keycloak.UserRepresentation;
import com.rog.EShop.properties.KeycloakProperties;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping(path = "/api")
public class KeycloakController {

    private final KeycloakProperties keycloakProperties;
    private final WebClient webClient;

    public KeycloakController(KeycloakProperties keycloakProperties) {
        this.keycloakProperties = keycloakProperties;
        webClient = WebClient.create(keycloakProperties.getHost());
    }



    @PostMapping("/token")
    public Mono<AccessTokenResponse> getToken(@RequestBody TokenDto tokenDto) {
        MultiValueMap<String, String> formData = new LinkedMultiValueMap<>();
        formData.add("username", tokenDto.getUsername());
        formData.add("password", tokenDto.getPassword());
        formData.add("client_id", tokenDto.getClientId());
        formData.add("client_secret", tokenDto.getClientSecret());
        formData.add("grant_type", tokenDto.getGrantType());

        return webClient.post()
                .uri("/realms/" + keycloakProperties.getRealm() + "/protocol/openid-connect/token")
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_FORM_URLENCODED_VALUE)
                .body(BodyInserters.fromFormData(formData))
                .retrieve()
                .bodyToMono(AccessTokenResponse.class);
    }

    @PostMapping("/keycloak_user")
    public Mono<String> create(@RequestHeader MultiValueMap<String, String> headers,
                               @RequestBody UserRepresentation userRepresentation) {

        return webClient.post()
                .uri("/admin/realms/" + keycloakProperties.getRealm() + "/users")
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .header(HttpHeaders.AUTHORIZATION, headers.getFirst(HttpHeaders.AUTHORIZATION.toLowerCase()))
                .body(Mono.just(userRepresentation), UserRepresentation.class)
                .retrieve()
                .bodyToMono(String.class);
    }

}
