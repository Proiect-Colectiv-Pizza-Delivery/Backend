package com.pizza.security.payload.request;

import lombok.Data;

import java.util.HashSet;
import java.util.Set;

@Data
public class UserUpdateRequest {

    private Boolean accountNonExpired;

    private Boolean accountNonLocked;

    private Boolean credentialsNonExpired;

    private Boolean enabled;

    private Set<String> roles = new HashSet<>();
}
