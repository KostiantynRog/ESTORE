package com.rog.EShop.entity;

import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Entity
@Table(name = "USERS")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "users_id_generator")
    @SequenceGenerator(name = "users_id_generator", sequenceName = "user_id_seq", allocationSize = 1)
    private Long id;
    private UUID keycloakId;
    private String username;
//    private String email;
//    private String password;
    private String firstName;
    private String lastName;

//    private OffsetDateTime registerDate;
    private LocalDateTime registerDate;

//    @ElementCollection(targetClass = Role.class)
//    @CollectionTable(name = "users_roles", joinColumns = @JoinColumn(name = "user_id"))
//    @Column(name = "role")
//    @Enumerated(EnumType.STRING)
//    private List<Role> roles;


//    public void setAuthorities(List<Role> roles) {
//        this.roles = roles;
//    }

    public void setRegisterDate(LocalDateTime registerDate) {
        this.registerDate = registerDate;
    }

    public LocalDateTime getRegisterDate() {
        return registerDate;
    }



    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public UUID getKeycloakId() {
        return keycloakId;
    }

    public void setKeycloakId(UUID keycloakId) {
        this.keycloakId = keycloakId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

//    public void setPassword(String password) {
//        this.password = password;
//    }

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

//    public String getEmail() {
//        return email;
//    }
//
//    public void setEmail(String email) {
//        this.email = email;
//    }

//    public String getPassword() {
//        return password;
//    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return Objects.equals(id, user.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
