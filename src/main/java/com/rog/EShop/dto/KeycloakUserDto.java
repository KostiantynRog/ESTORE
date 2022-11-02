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
    private List<Role> realmRoles;
    private List<Credential> credentials;

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
