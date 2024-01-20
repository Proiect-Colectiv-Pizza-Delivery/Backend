package com.pizza.security;


import com.pizza.exception.CustomException;
import com.pizza.model.security.requests.LoginRequest;
import com.pizza.model.security.requests.SignupRequest;
import com.pizza.model.security.responses.JwtResponse;
import com.pizza.model.security.user.ERole;
import com.pizza.model.security.user.Role;
import com.pizza.model.security.user.User;
import com.pizza.repository.security.RoleRepository;
import com.pizza.repository.security.UserRepository;
import com.pizza.security.jwt.JwtUtils;
import com.pizza.service.security.TokenService;
import jakarta.transaction.Transactional;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Log4j2
@Service
public class UserDetailsManagerImpl implements UserDetailsManager {

    @Autowired
    UserRepository userRepo;

    @Autowired
    RoleRepository roleRepo;

    @Autowired
    JwtUtils jwtUtils;

    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    TokenService tokenService;

    public void createUser(UserDetails userDetails, String deviceId) {
        User user = new User(userDetails.getUsername(), userDetails.getPassword(),
                userDetails.getAuthorities().stream()
                        .map(authority -> new Role(ERole.valueOf(authority.getAuthority())))
                        .collect(Collectors.toSet()), deviceId);
        userRepo.save(user);
    }

    @Override
    @Transactional
    public UserDetailsImpl loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepo.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User Not Found with username: " + username));

        return UserDetailsImpl.build(user);
    }

    public String getDeviceIdForUser(String username) throws UsernameNotFoundException {
        User user = userRepo.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User Not Found with username: " + username));
        return user.getDeviceId();
    }

    public void changeDeviceIdForUser(String deviceId, String username) {
        User user = userRepo.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User Not Found with username: " + username));
        user.setDeviceId(deviceId);
        userRepo.save(user);
    }

    public JwtResponse loginUser(LoginRequest loginRequest, String deviceId) {
        Authentication authentication = authenticationManager
                .authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);

        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        String username = userDetails.getUsername();

        String encodedDeviceId = jwtUtils.encodeDeviceId(deviceId);
        changeDeviceIdForUser(encodedDeviceId, username);

        String accessJwtToken = jwtUtils.generateAccessJwtToken(username, deviceId);

        List<String> roles = userDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .toList();

        String accessTokenId = jwtUtils.getAccessTokenIdFromJwtToken(accessJwtToken);
        String refreshJwtToken = jwtUtils.generateRefreshJwtToken(username, deviceId, accessTokenId);

        return new JwtResponse(accessJwtToken, refreshJwtToken,
                userDetails.getUsername(), userDetails.getEmail(), userDetails.getFirstName(),
                userDetails.getLastName(), userDetails.getPhoneNumber(), roles);

    }

    public void registerUser(SignupRequest signUpRequest, String deviceId) throws CustomException {
        if (userRepo.existsByUsername(signUpRequest.getUsername())) {
            String error="Username already in use";
            log.error(error);
            throw new CustomException(error);
        }
        if (userRepo.existsByPhoneNumber(signUpRequest.getPhoneNumber())) {
            String error="Phone number already in use";
            log.error(error);
            throw new CustomException(error);
        }
        if (userRepo.existsByEmail(signUpRequest.getEmail())) {
            String error="Email already in use";
            log.error(error);
            throw new CustomException(error);
        }
        Set<Role> roles = new HashSet<>();
        Role roleStart = roleRepo.findByName(ERole.valueOf(signUpRequest.getRole()))
                .orElseThrow(() -> new CustomException("Error: Role is not found."));
        roles.add(roleStart);

        List<GrantedAuthority> authorities = roles.stream()
                .map(role -> new SimpleGrantedAuthority(role.getName().name()))
                .collect(Collectors.toList());

        signUpRequest.setPassword(passwordEncoder.encode(signUpRequest.getPassword()));
        UserDetailsImpl userDetails = new UserDetailsImpl(signUpRequest, authorities);

        String encodedDeviceId = jwtUtils.encodeDeviceId(deviceId);
        this.createUser(userDetails, encodedDeviceId);

        String username = signUpRequest.getUsername();
        User user = userRepo.findByUsername(username)
                .orElseThrow(() -> new CustomException("User Not Found with username: " + username));

        user.setEmail(signUpRequest.getEmail());
        user.setFirstName(signUpRequest.getFirstName());
        user.setLastName(signUpRequest.getLastName());
        user.setPhoneNumber(signUpRequest.getPhoneNumber());

        userRepo.save(user);
    }

    @Override
    public void createUser(UserDetails userDetails) {
    }

    @Override
    public void updateUser(UserDetails userDetails) {
    }

    @Override
    @Transactional
    public void deleteUser(String username) {
    }

    @Override
    public void changePassword(String oldPassword, String newPassword) {
    }

    @Override
    public boolean userExists(String username) {
        return userRepo.existsByUsername(username);
    }

}
