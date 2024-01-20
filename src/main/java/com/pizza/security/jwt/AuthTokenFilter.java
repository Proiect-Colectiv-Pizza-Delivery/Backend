package com.pizza.security.jwt;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.pizza.exception.security.ExpiredJwtException;
import com.pizza.exception.security.InvalidJwtForPathException;
import com.pizza.exception.security.MissingDeviceIdException;
import com.pizza.repository.security.InvalidatedTokenRepository;
import com.pizza.security.UserDetailsManagerImpl;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Log4j2
public class AuthTokenFilter extends OncePerRequestFilter {
    @Autowired
    private JwtUtils jwtUtils;

    @Autowired
    private UserDetailsManagerImpl userDetailsManager;

    @Autowired
    private InvalidatedTokenRepository invalidatedTokenRepository;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        String deviceHeader = parseDeviceId(request);
        if (deviceHeader == null) {
            handleMissingDeviceId(response);
            return;
        }

        try {
            String jwt = parseJwt(request);

            if (jwt != null) {
                if (jwtUtils.validateJwtToken(jwt)) {
                    if(!jwtUtils.validateJwtTokenAndDeviceId(jwt,deviceHeader)){
                        handleJwtDeviceIdAndDeviceIdMismatch(response);
                        return;
                    }
                    handleTokenAuthentication(request, jwt, deviceHeader);
                }
            } else {
                if (!request.getServletPath().equals("/api/auth/signup") && !request.getServletPath().equals("/api/auth/login") && !request.getServletPath().equals("/api/auth/loginGoogle")) {
                    handleMissingJwt(response);
                    return;
                }
            }
        } catch (Exception e) {
            log.error("Cannot set user authentication or missing/expired/invalid jwt for path: {}", e.getMessage());
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, e.getMessage());
            return;
        }

        filterChain.doFilter(request, response);
    }

    private void handleTokenAuthentication(HttpServletRequest request, String jwt, String deviceHeader) {
        String requestedPath = request.getServletPath();

        boolean isRefreshTokenPath = requestedPath.equals("/api/auth/refreshtoken") || requestedPath.equals("/api/auth/signout");
        boolean isRefreshToken = JwtUtils.REFRESH_TOKEN_ID.equals(jwtUtils.getJwtTokenType(jwt));

        if ((isRefreshTokenPath && !isRefreshToken) || (!isRefreshTokenPath && isRefreshToken)) {
            throw new InvalidJwtForPathException("Invalid JWT type for the requested path");
        }

        String username = jwtUtils.getUserNameFromJwtToken(jwt);
        UserDetails userDetails = userDetailsManager.loadUserByUsername(username);

        if (!isRefreshToken) {
            if (invalidatedTokenRepository.existsById(jwtUtils.getAccessTokenIdFromJwtToken(jwt))) {
                throw new ExpiredJwtException("Expired jwt");
            }

            if (!jwtUtils.checkRawAgainstEncoded(deviceHeader, userDetailsManager.getDeviceIdForUser(username))) {
                throw new MissingDeviceIdException("Wrong deviceId");
            }

            UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                    userDetails, null, userDetails.getAuthorities());
            authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
            SecurityContextHolder.getContext().setAuthentication(authentication);
        } else {
            if (invalidatedTokenRepository.existsById(jwtUtils.getRefreshTokenIdFromJwtToken(jwt))) {
                throw new ExpiredJwtException("Expired jwt");
            }
            if (!jwtUtils.checkRawAgainstEncoded(deviceHeader, userDetailsManager.getDeviceIdForUser(username))) {
                throw new MissingDeviceIdException("Wrong deviceId");
            }
        }
    }

    private String parseJwt(HttpServletRequest request) {
        String headerAuth = request.getHeader("Token");

        if (StringUtils.hasText(headerAuth) && headerAuth.startsWith("Bearer ")) {
            return headerAuth.substring(7);
        }

        return null;
    }

    private String parseDeviceId(HttpServletRequest request) {
        String deviceHeader = request.getHeader("Device-id");

        if (StringUtils.hasText(deviceHeader)) {
            return deviceHeader;
        }

        return null;
    }

    private void handleMissingDeviceId(HttpServletResponse response) throws IOException {

        ObjectMapper mapper = new ObjectMapper();
        Map<String, Object> errorDetails = new HashMap<>();
        errorDetails.put("error", "Missing Device ID");

        response.setStatus(HttpStatus.UNAUTHORIZED.value());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.getWriter().write(mapper.writeValueAsString(errorDetails));

    }

    private void handleMissingJwt(HttpServletResponse response) throws IOException {

        ObjectMapper mapper = new ObjectMapper();
        Map<String, Object> errorDetails = new HashMap<>();
        errorDetails.put("error", "Missing jwt");

        response.setStatus(HttpStatus.UNAUTHORIZED.value());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.getWriter().write(mapper.writeValueAsString(errorDetails));

    }

    private void handleJwtDeviceIdAndDeviceIdMismatch(HttpServletResponse response) throws IOException {

        ObjectMapper mapper = new ObjectMapper();
        Map<String, Object> errorDetails = new HashMap<>();
        errorDetails.put("error", "Jwt device id and device id dont match");

        response.setStatus(HttpStatus.UNAUTHORIZED.value());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.getWriter().write(mapper.writeValueAsString(errorDetails));


    }

}