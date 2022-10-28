package com.rog.EShop.controllers;

import com.rog.EShop.dto.KeycloakUserDto;
import com.rog.EShop.dto.TokenDto;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@RestController
public class KeycloakController {
    WebClient webClient = WebClient.create("http://localhost:8080");

    public Mono<TokenDto> getToken(@RequestBody TokenDto tokenDto) {
        return webClient.post()
                .uri("/realms/ESTORE/protocol/openid-connect/token")
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_FORM_URLENCODED_VALUE)
                .body(Mono.just(tokenDto), TokenDto.class)
                .retrieve()
                .bodyToMono(TokenDto.class);
    }

    public Mono<KeycloakUserDto> create(KeycloakUserDto keycloakUserDto) {
        return webClient.post()
                .uri("/admin/realms/ESTORE/user")
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .body(Mono.just(keycloakUserDto), KeycloakUserDto.class)
                .retrieve()
                .bodyToMono(KeycloakUserDto.class);
    }
}
