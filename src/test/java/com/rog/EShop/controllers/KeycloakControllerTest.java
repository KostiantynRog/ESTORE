package com.rog.EShop.controllers;

import com.rog.EShop.EShopApplication;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static com.nimbusds.jose.HeaderParameterNames.CONTENT_TYPE;

import static org.springframework.test.web.client.match.MockRestRequestMatchers.jsonPath;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {EShopApplication.class })
@WebAppConfiguration
class KeycloakControllerTest {
    @Autowired
    private WebApplicationContext wac;
    private MockMvc mockMvc;
    @BeforeEach
    public void setUp() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.wac).build();
    }

    @Test
    void getToken() throws Exception {
        this.mockMvc.perform(post("localhost:8080/realms/ESTORE/protocol/openid-connect/token").param("username", "user5").param("password", "user5").param("client_id", "estore-api").param("client_secret", "2iwoTq3NgMMj0CRHIjzWbZ7GRLj5mLEk").param("grant_type", "password")).andDo(print()).andExpect(status().isOk()).andExpect(content().contentType(CONTENT_TYPE))
                .andExpect(jsonPath("").value("")).andExpect(jsonPath("$.id").value(1));
    }

    @Test
    void create() {
    }
}