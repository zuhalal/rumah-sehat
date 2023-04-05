package com.tugaskelompok.rumahsehat.config.auth;

import java.io.IOException;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

// taken from https://www.javainuse.com/spring/boot-jwt-mysql
@Component
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint, Serializable {
    private static final long serialVersionUID = -7858869558953243875L;

    static final Logger logger = LoggerFactory.getLogger(JwtAuthenticationEntryPoint.class);

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response,
                         AuthenticationException authException) throws IOException {
        var now = LocalDateTime.now().toString();

        Map<String, String> err = new HashMap<>();

        if (request.getServletPath().equals("/api/v1/auth/login")) {

            logger.warn("Invalid credentials login attempt in {} at {}", request.getRequestURI(), now);

            err.put("message", "Login authentication failed. Invalid username or password");
        } else {
            logger.warn("Unauthorized request attempt in {} at {}", request.getRequestURI(), now);
            err.put("message", "Access is Denied");
        }

        response.setContentType("application/json");
        response.setStatus(401);
        new ObjectMapper().writeValue(response.getOutputStream(), err);
    }
}