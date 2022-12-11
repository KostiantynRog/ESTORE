package com.rog.EShop.dto;

import com.rog.EShop.entity.Role;

import java.util.List;
import java.util.UUID;

public class ResponseKeycloakDto {
    private UUID id;
    private Long createdTimestamp;
    private String username;
    private String firstName;
    private String lastName;
    private List<Role> realmRoles;

    public List<Role> getRealmRoles() {
        return realmRoles;
    }

    public void setRealmRoles(List<Role> realmRoles) {
        this.realmRoles = realmRoles;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public Long getCreatedTimestamp() {
        return createdTimestamp;
    }

    public void setCreatedTimestamp(Long createdTimestamp) {
        this.createdTimestamp = createdTimestamp;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }


    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }


}
