package com.pizza.security.payload.response;

import com.pizza.model.Role;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Set;

@Data
@AllArgsConstructor
public class UserResponse {

    private String username;

    private String email;

    private String firstName;

    private String lastName;

    private String phoneNumber;

    private Set<Role> roles;
}
