package com.rog.EShop.dto;

import com.rog.EShop.entity.Role;

import java.util.List;

public class KeycloakUserDto {
    private String firstName;
    private String lastName;
    private String username;
    private String email;
    private Boolean emailVerified;
    private Boolean enabled;
//    private List<RealmRole> roles;
//    private List<Credential> credentials;
}
