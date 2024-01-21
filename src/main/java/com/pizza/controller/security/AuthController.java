package com.pizza.controller.security;

import com.pizza.exception.AlreadyInUseException;
import com.pizza.exception.CustomException;
import com.pizza.exception.security.TokenRefreshException;
import com.pizza.model.security.requests.LoginRequest;
import com.pizza.model.security.requests.SignupRequest;
import com.pizza.model.security.responses.JwtResponse;
import com.pizza.model.security.responses.TokenRefreshResponse;
import com.pizza.security.UserDetailsManagerImpl;
import com.pizza.service.security.TokenService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.security.GeneralSecurityException;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/auth")
public class AuthController {
    UserDetailsManagerImpl userDetailsManager;
    TokenService tokenService;

    @Autowired
    public AuthController(UserDetailsManagerImpl userDetailsManager, TokenService tokenService) {
        this.userDetailsManager = userDetailsManager;
        this.tokenService = tokenService;
    }

    @PostMapping("/login")
    public ResponseEntity<JwtResponse> loginUser(@Valid @RequestBody LoginRequest loginRequest,
                                                 @RequestHeader(name="Device-id") String deviceId) {
        try {
            return ResponseEntity.ok(userDetailsManager.loginUser(loginRequest,deviceId));
        } catch (UsernameNotFoundException exception) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/loginGoogle")
    public ResponseEntity<JwtResponse> loginUserGoogle(@RequestHeader(name="googleHeader") String googleHeader,
                                                 @RequestHeader(name="Device-id") String deviceId) throws CustomException {
        try {
            return ResponseEntity.ok(userDetailsManager.loginUserGoogle(googleHeader,deviceId));
        } catch (UsernameNotFoundException | GeneralSecurityException | IOException exception ) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/signup")
    public ResponseEntity<String> registerUser(@Valid @RequestBody SignupRequest signUpRequest,@RequestHeader(name="Device-id") String deviceId) throws CustomException, AlreadyInUseException {
        userDetailsManager.registerUser(signUpRequest, deviceId);
        return ResponseEntity.ok("User " + signUpRequest.getUsername() + " registered successfully!");
    }

    @PostMapping("/refreshtoken")
    public ResponseEntity<TokenRefreshResponse> refreshToken(@RequestHeader(name="Token") String token, @RequestHeader(name="Device-id") String deviceId) {
        try {
            return tokenService.processRefreshTokenRequest(token.substring(7),deviceId);
        } catch (TokenRefreshException exception) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/signout")
    public ResponseEntity<String> logoutUser(@RequestHeader(name="Token") String token) {
        try {
            tokenService.invalidateTokens(token.substring(7));
        } catch (UsernameNotFoundException exception) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok("Log out successful!");
    }

}