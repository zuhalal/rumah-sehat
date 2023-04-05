package com.tugaskelompok.rumahsehat.config.auth;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tugaskelompok.rumahsehat.config.ApiResponseKey;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import io.jsonwebtoken.ExpiredJwtException;

// taken from https://www.javainuse.com/spring/boot-jwt-mysql
@Component
public class JwtRequestFilter extends OncePerRequestFilter {
    @Qualifier("jwtUserDetailsService")
    @Autowired
    private UserDetailsService jwtUserDetailsService;
    @Autowired
    private JwtTokenUtils jwtTokenUtils;
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
        throws ServletException, IOException {

        var contentType = "application/json";

        if (request.getServletPath().contains("/api/v1/") && (!request.getServletPath().equals("/api/v1/auth/login")) && (!request.getServletPath().equals("/api/v1/auth/regis"))) {
            final String requestTokenHeader = request.getHeader("Authorization");

            String username = null;
            String jwtToken = null;
            // JWT Token is in the form "Bearer token". Remove Bearer word and get
            // only the Token
            if (requestTokenHeader != null && requestTokenHeader.startsWith("Bearer ")) {
                jwtToken = requestTokenHeader.substring(7);

                try {
                    username = jwtTokenUtils.getUsernameFromToken(jwtToken);
                } catch (IllegalArgumentException e) {
                    logger.warn("Unable to get JWT Token");

                    Map<String, String> err = new HashMap<>();
                    err.put(ApiResponseKey.MESSAGE, e.getMessage());
                    response.setContentType(contentType);
                    response.setStatus(401);
                    new ObjectMapper().writeValue(response.getOutputStream(), err);
                } catch (ExpiredJwtException e) {
                    logger.warn("JWT Token has expired");

                    Map<String, String> err = new HashMap<>();
                    err.put(ApiResponseKey.MESSAGE, e.getMessage());
                    response.setContentType(contentType);
                    response.setStatus(401);
                    new ObjectMapper().writeValue(response.getOutputStream(), err);
                } catch (Exception e) {
                    var now = LocalDateTime.now().toString();
                    logger.warn("Unauthorized Access Request at " + now);
                    Map<String, String> err = new HashMap<>();
                    err.put(ApiResponseKey.MESSAGE, "Access is denied");
                    response.setContentType(contentType);
                    response.setStatus(401);
                    new ObjectMapper().writeValue(response.getOutputStream(), err);
                }
            } else {
                logger.warn("JWT Token does not begin with Bearer String");

                Map<String, String> err = new HashMap<>();
                err.put(ApiResponseKey.MESSAGE, "JWT Token does not begin with Bearer String");
                response.setContentType(contentType);
                response.setStatus(401);
                new ObjectMapper().writeValue(response.getOutputStream(), err);
            }
            // Once we get the token validate it.
            if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                var userDetails = this.jwtUserDetailsService.loadUserByUsername(username);
                // authentication
                if (Boolean.TRUE.equals(jwtTokenUtils.validateToken(jwtToken, userDetails))) {
                    var usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(
                        userDetails, null, userDetails.getAuthorities());
                    usernamePasswordAuthenticationToken
                            .setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    // After setting the Authentication in the context, we specify
                    // that the current user is authenticated. So it passes the
                    // Spring Security Configurations successfully.
                    SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
                }
            }
        }

        try {
            chain.doFilter(request, response);
        } catch (Exception e) {
            logger.warn("Invalid authenticated request attempt in " + request.getRequestURI());
        }
    }
}