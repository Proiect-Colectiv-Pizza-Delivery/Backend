package com.pizza.repository.security;

import com.pizza.model.security.user.ERole;
import com.pizza.model.security.user.Role;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;


public interface RoleRepository extends JpaRepository<Role, Long> {
    Optional<Role> findByName(ERole name);
}