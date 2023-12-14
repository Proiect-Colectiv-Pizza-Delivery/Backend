package com.pizza.security.payload.response;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;
@Data
@AllArgsConstructor
public class JwtResponse {

    private String token;

    private final String type = "Bearer";

    private String refreshToken;

    private String username;

    private String email;

    private String firstName;

    private String lastName;

    private String phoneNumber;

    private List<String> roles;

    public JwtResponse(String username, String email, String firstName, String lastName, String phoneNumber, List<String> roles) {
        this.username = username;
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
        this.phoneNumber = phoneNumber;
        this.roles = roles;
    }
}