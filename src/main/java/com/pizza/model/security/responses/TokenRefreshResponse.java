package com.pizza.model.security.responses;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class TokenRefreshResponse {

    private String accessToken;

    private String refreshToken;

    private final String tokenType = "Bearer";

}