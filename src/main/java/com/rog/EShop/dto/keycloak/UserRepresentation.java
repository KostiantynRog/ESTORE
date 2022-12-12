package com.rog.EShop.dto.keycloak;

import com.rog.EShop.dto.Credential;
import com.rog.EShop.entity.Role;

import java.util.List;
import java.util.UUID;

public class UserRepresentation {
    private UUID id;
    private Long createdTimestamp;
    private String username;
    private String firstName;
    private String lastName;
    private List<Role> realmRoles;

    private String email;
    private Boolean emailVerified;
    private Boolean enabled;

    private List<Credential> credentials;

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

    public List<Role> getRealmRoles() {
        return realmRoles;
    }

    public void setRealmRoles(List<Role> realmRoles) {
        this.realmRoles = realmRoles;
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

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Boolean getEmailVerified() {
        return emailVerified;
    }

    public void setEmailVerified(Boolean emailVerified) {
        this.emailVerified = emailVerified;
    }

    public Boolean getEnabled() {
        return enabled;
    }

    public void setEnabled(Boolean enabled) {
        this.enabled = enabled;
    }


    public List<Credential> getCredentials() {
        return credentials;
    }

    public void setCredentials(List<Credential> credentials) {
        this.credentials = credentials;
    }
}
