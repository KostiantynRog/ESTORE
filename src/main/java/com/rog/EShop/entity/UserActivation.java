package com.rog.EShop.entity;

import jakarta.persistence.*;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Entity
@Table(name = "USER_ACTIVATION")
@Component
public class UserActivation {
    @Id
    @SequenceGenerator(name = "user_activation_id_generator", sequenceName = "user_activation_id_seq",
            allocationSize = 1)
    private String userId;
    private String activationToken;
    private LocalDateTime tokenExpiryDate;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getActivationToken() {
        return activationToken;
    }

    public void setActivationToken(String activationToken) {
        this.activationToken = activationToken;
    }

    public LocalDateTime getTokenExpiryDate() {
        return tokenExpiryDate;
    }

    public void setTokenExpiryDate(LocalDateTime tokenExpiryDate) {
        this.tokenExpiryDate = tokenExpiryDate;
    }
}
