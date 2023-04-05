package com.tugaskelompok.rumahsehat.auth.restcontroller;

import com.tugaskelompok.rumahsehat.auth.data_transfer_object.*;
import com.tugaskelompok.rumahsehat.config.ApiResponseKey;
import com.tugaskelompok.rumahsehat.config.auth.JwtTokenUtils;
import com.tugaskelompok.rumahsehat.pasien.model.PasienModel;
import com.tugaskelompok.rumahsehat.pasien.service.PasienService;
import com.tugaskelompok.rumahsehat.user.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.HashMap;

@RestController
@CrossOrigin
@RequestMapping("/api/v1")
public class AuthRestController {
    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtTokenUtils jwtTokenUtils;

    @Autowired
    UserService userService;

    @Qualifier("jwtUserDetailsService")
    @Autowired
    private UserDetailsService jwtUserDetailsService;

    @Autowired
    private PasienService pasienService;

    private Logger logger = LoggerFactory.getLogger(AuthRestController.class);

    @PostMapping(value = "/auth/login")
    public ResponseEntity<Object> createAuthenticationToken(@Valid @RequestBody JwtRequestDTO authenticationRequest) {
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(authenticationRequest.getUsername(), authenticationRequest.getPassword()));
            var userDetails = jwtUserDetailsService.loadUserByUsername(authenticationRequest.getUsername());

            logger.info("Pasien Successfully login");

            final String token = jwtTokenUtils.generateToken(userDetails);
            return ResponseEntity.ok(new JwtResponseDTO(token));
        } catch (UsernameNotFoundException exception) {
            logger.error(exception.getMessage());
            HashMap<String, Object> map = new HashMap<>();
            map.put(ApiResponseKey.MESSAGE, "Invalid credentials");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(map);
        } catch (BadCredentialsException exception) {
            logger.error(exception.getMessage());
            HashMap<String, Object> map = new HashMap<>();
            map.put(ApiResponseKey.MESSAGE, "Invalid username or password");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(map);
        } catch (Exception exception) {
            logger.error(exception.getMessage());
            HashMap<String, Object> map = new HashMap<>();
            map.put(ApiResponseKey.MESSAGE, "Error when creating token");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(map);
        }
    }

    @GetMapping("/auth/details")
    public ResponseEntity<HashMap<String, Object>> getUserDetail(HttpServletRequest request) {
        try {
            var now  = LocalDateTime.now().toString();

            logger.info("API Request in {} at {}", request.getRequestURI(), now);

            final String requestTokenHeader = request.getHeader("Authorization");
            var jwtToken = requestTokenHeader.substring(7);

            String username = jwtTokenUtils.getUsernameFromToken(jwtToken);

            PasienModel pasien = pasienService.getPasienByUsername(username);

            HashMap<String, Object> map = new HashMap<>();
            map.put(ApiResponseKey.STATUS, "success");

            if (pasien != null) {
                logger.info("User details found");
                map.put(ApiResponseKey.STATUS_CODE, 200);
                map.put(ApiResponseKey.MESSAGE, "Detail User succesfully retrieved");
                map.put(ApiResponseKey.DATA, pasien);
                logger.info("200 OK request in {}", request.getRequestURI());
                return ResponseEntity.ok(map);
            } else {
                logger.error("User details not found");
                map.put(ApiResponseKey.STATUS, 401);
                map.put(ApiResponseKey.MESSAGE, "Detail User not found");
                map.put(ApiResponseKey.DATA, null);
                logger.info("401 Unauthorized request in {}", request.getRequestURI());
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(map);
            }
        } catch (Exception e) {
            logger.error("500 Internal server error request in {}", request.getRequestURI());
            HashMap<String, Object> map = new HashMap<>();
            map.put(ApiResponseKey.STATUS, "failed");
            map.put(ApiResponseKey.STATUS_CODE, 500);
            map.put(ApiResponseKey.MESSAGE, e.getMessage());
            map.put(ApiResponseKey.DATA, null);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(map);
        }
    }

    @PostMapping(value = "/auth/regis")
    public ResponseEntity<HashMap<String, Object>> regisPasien(
        @RequestBody RegisRequestDTO pasienDTO) {
        try {
            HashMap<String, Object> map = new HashMap<>();
            map.put(ApiResponseKey.STATUS, "success");

            if (userService.getUserByUsername(pasienDTO.getUsername()) != null){
                logger.error("Registration failed because username already exist");
                map.put(ApiResponseKey.STATUS_CODE, 400);
                map.put(ApiResponseKey.MESSAGE, "Username already exist");
                map.put(ApiResponseKey.DATA, null);
                return ResponseEntity.ok(map);
            } else if(userService.getUserByEmail(pasienDTO.getEmail()) != null){
                logger.error("Registration failed because email already exist");
                map.put(ApiResponseKey.STATUS_CODE, 400);
                map.put(ApiResponseKey.MESSAGE, "Email already exist");
                map.put(ApiResponseKey.DATA, null);
                return ResponseEntity.ok(map);
            } else {
                logger.info("Registration Successful");
                PasienModel newPasien = pasienService.addPasien(pasienDTO.getNama(), pasienDTO.getUsername(), pasienDTO.getPassword(), pasienDTO.getUmur(), pasienDTO.getEmail());
                var pasienResponseDTO = new RegisResponseDTO(newPasien.getUuid(), newPasien.getUsername(), newPasien.getNama(), newPasien.getEmail(), newPasien.getUmur(), newPasien.getSaldo(), newPasien.getIsSso(), newPasien.getRole());
                map.put(ApiResponseKey.STATUS_CODE, 200);
                map.put(ApiResponseKey.MESSAGE, "Your account has been created");
                map.put(ApiResponseKey.DATA, pasienResponseDTO);
            }

            logger.info("200 OK request in /api/v1/auth/regis");
            return ResponseEntity.ok(map);
        } catch (Exception e) {
            logger.error("500 Internal Server Error request in /api/v1/auth/regis");
            HashMap<String, Object> map = new HashMap<>();
            map.put(ApiResponseKey.STATUS, "failed");
            map.put(ApiResponseKey.MESSAGE, e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(map);
        }
    }
}
