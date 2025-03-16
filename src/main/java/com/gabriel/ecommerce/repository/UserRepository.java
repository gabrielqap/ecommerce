package com.gabriel.ecommerce.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.core.userdetails.UserDetails;

import com.gabriel.ecommerce.entity.User;
import com.gabriel.ecommerce.enums.Role;

public interface UserRepository extends JpaRepository<User, String> {
    UserDetails findByUsername(String username);
    List<User> findByRole(Role role);
}