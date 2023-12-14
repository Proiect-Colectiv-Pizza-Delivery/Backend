package com.pizza.controller;


import com.pizza.security.RefreshTokenService;
import com.pizza.security.TokenRefreshException;
import com.pizza.security.UserDetailsManagerImpl;
import com.pizza.security.payload.request.LoginRequest;
import com.pizza.security.payload.request.SignupRequest;
import com.pizza.security.payload.response.JwtResponse;
import com.pizza.security.payload.response.TokenRefreshResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/auth")
@SecurityRequirement(name = "bearerAuth")
public class AuthController {
    UserDetailsManagerImpl userDetailsManager;
    RefreshTokenService refreshTokenService;

    @Autowired
    public AuthController(UserDetailsManagerImpl userDetailsManager, RefreshTokenService refreshTokenService) {
        this.userDetailsManager = userDetailsManager;
        this.refreshTokenService = refreshTokenService;
    }

    @PostMapping("/login")
    public ResponseEntity<JwtResponse> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {
        try {
            return ResponseEntity.ok(userDetailsManager.loginUser(loginRequest));
        } catch (UsernameNotFoundException exception) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/signup")
    public ResponseEntity<String> registerUser(@Valid @RequestBody SignupRequest signUpRequest) {
        try {
            userDetailsManager.registerUser(signUpRequest);
        } catch (RuntimeException exception) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok("User " + signUpRequest.getUsername() + " registered successfully!");
    }

    @PostMapping("/refreshtoken")
    public ResponseEntity<TokenRefreshResponse> refreshToken(@Valid @RequestBody @NotBlank String refreshToken) {
        try {
            return refreshTokenService.createAuthToken(refreshToken);
        } catch (TokenRefreshException exception) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/signout")
    public ResponseEntity<String> logoutUser() {
        try {
            String username = userDetailsManager.logoutUser();
            refreshTokenService.deleteByUsername(username);
        } catch (UsernameNotFoundException exception) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok("Log out successful!");
    }

}