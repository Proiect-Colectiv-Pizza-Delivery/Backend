package com.pizza.security.payload.request;

import jakarta.validation.constraints.Email;
import lombok.Data;

@Data
public class UserDataRequest {

    @Email
    private String email;

    private String firstName;

    private String lastName;

    private String phoneNumber;
}
