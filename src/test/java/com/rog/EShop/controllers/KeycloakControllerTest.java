package com.rog.EShop.controllers;

import com.rog.EShop.dto.TokenDto;
import com.rog.EShop.properties.KeycloakProperties;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;


import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@SpringBootTest
class KeycloakControllerTest {
    @Autowired
    private WebApplicationContext wac;
    private MockMvc mockMvc;
    private KeycloakProperties keycloakProperties;

    @BeforeEach
    public void setUp() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.wac).build();
    }

    @Test
    void getToken() throws Exception {
        TokenDto tokenDto = new TokenDto();
        tokenDto.setUsername("user5");
        tokenDto.setPassword("user5");
        tokenDto.setClientId(keycloakProperties.getClientId());
        tokenDto.setClientSecret(keycloakProperties.getClientSecret());
        tokenDto.setGrantType("password");
        this.mockMvc.perform(post("/api/token").param(tokenDto.getUsername()))
                .andDo(print()).andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("access_token").isNotEmpty());
    }

    @Test
    void create() {
    }
}