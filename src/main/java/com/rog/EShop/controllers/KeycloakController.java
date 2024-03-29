package com.rog.EShop.controllers;

import com.rog.EShop.dto.TokenDto;
import com.rog.EShop.properties.ApplicationProperties;
import org.keycloak.representations.AccessTokenResponse;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;


@RestController
@Deprecated
@RequestMapping(path = "/api")
public class KeycloakController {

    private final ApplicationProperties applicationProperties;
    private final WebClient webClient;

    public KeycloakController(ApplicationProperties applicationProperties) {
        this.applicationProperties = applicationProperties;
        webClient = WebClient.create(applicationProperties.getKeycloak().getBaseUrl());
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
                .uri("/realms/" + applicationProperties.getKeycloak().getRealm() + "/protocol/openid-connect/token")
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_FORM_URLENCODED_VALUE)
                .body(BodyInserters.fromFormData(formData))
                .retrieve()
                .bodyToMono(AccessTokenResponse.class);
    }



}
