package com.rog.EShop.repository;

import com.rog.EShop.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
    boolean existsUserByUserName(String username);
}
