package com.tugaskelompok.rumahsehat.pasien.restcontroller;

import com.tugaskelompok.rumahsehat.config.*;
import com.tugaskelompok.rumahsehat.config.auth.JwtTokenUtils;
import com.tugaskelompok.rumahsehat.pasien.data_transfer_object.PasienTopUpRequest;
import com.tugaskelompok.rumahsehat.pasien.model.PasienModel;
import com.tugaskelompok.rumahsehat.pasien.service.PasienRestService;
import com.tugaskelompok.rumahsehat.pasien.service.PasienService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.web.firewall.RequestRejectedException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.HashMap;
import java.util.Locale;
import java.util.NoSuchElementException;

@RestController
@CrossOrigin
@RequestMapping("/api/v1")
public class  PasienRestController {
    @Autowired
    PasienService pasienService;

    @Autowired
    PasienRestService pasienRestService;

    @Autowired
    JwtTokenUtils jwtTokenUtils;

    @Autowired
    private MessageSource messageSource;

    Logger logger = LoggerFactory.getLogger(PasienRestController.class);

    @PostMapping("/pasien/profile")
    public ResponseEntity<HashMap<String, Object>> profilePasien(HttpServletRequest request) {
        try {

            final String requestTokenHeader = request.getHeader("Authorization");

            var jwtToken = requestTokenHeader.substring(7);

            String username = jwtTokenUtils.getUsernameFromToken(jwtToken);

            PasienModel pasien = pasienService.getPasienByUsername(username);

            HashMap<String, Object> res = new HashMap<>();

            res.put(ApiResponseKey.STATUS, "success");
            res.put(ApiResponseKey.STATUS_CODE, 200);

            res.put("username", pasien.getUsername());
            res.put("email", pasien.getEmail());
            res.put("nama", pasien.getNama());
            res.put("saldo", pasien.getSaldo());

            return ResponseEntity.ok(res);
        } catch (Exception e) {
            logger.error("Internal server error in {}", request.getRequestURI());

            HashMap<String, Object> res = new HashMap<>();
            res.put(ApiResponseKey.STATUS, RedirectAttributesKey.FAILED);
            res.put(ApiResponseKey.STATUS_CODE, 500);
            res.put(ApiResponseKey.MESSAGE, "There's something wrong on the server");
            res.put(ApiResponseKey.DATA, null);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(res);
        }
    }

    @PostMapping("/pasien/topup")
    public ResponseEntity<HashMap<String, Object>> topUpSaldo(HttpServletRequest request, @Valid @RequestBody PasienTopUpRequest pasienTopUpRequest, Errors
                                        errors, BindingResult bindingResult) {
        try {
            if (errors.hasErrors()) {
                throw new RequestRejectedException("Saldo harus bilangan positif");
            }

            final String requestTokenHeader = request.getHeader("Authorization");
            var jwtToken = requestTokenHeader.substring(7);

            String username = jwtTokenUtils.getUsernameFromToken(jwtToken);

            pasienRestService.topUpSaldo(username, pasienTopUpRequest.getNominal());

            PasienModel updatedPasien = pasienRestService.getPasienByUsername(username);

            HashMap<String, Object> pasienData = new HashMap<>();
            pasienData.put("username", updatedPasien.getUsername());
            pasienData.put("saldo", updatedPasien.getSaldo());

            HashMap<String, Object> res = new HashMap<>();
            res.put(ApiResponseKey.STATUS, RedirectAttributesKey.SUCCESS);
            res.put(ApiResponseKey.STATUS_CODE, 200);
            res.put(ApiResponseKey.MESSAGE, "Top-Up successful");
            res.put(ApiResponseKey.DATA, pasienData);

            return ResponseEntity.ok(res);
        } catch (RequestRejectedException e) {
            var error = bindingResult.getFieldError("nominal");
            var errorMessage = "";

            if (error != null) {
                errorMessage = messageSource.getMessage(error, Locale.getDefault());
            } else {
                errorMessage = "There's an request body error";
            }

            logger.warn("Request rejected in Top Up Saldo API Request: {}", errorMessage);

            HashMap<String, Object> res = new HashMap<>();

            res.put(ApiResponseKey.STATUS, RedirectAttributesKey.FAILED);
            res.put(ApiResponseKey.STATUS_CODE, 400);
            res.put(ApiResponseKey.MESSAGE, errorMessage);
            res.put(ApiResponseKey.DATA, null);

            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(res);
        } catch (NoSuchElementException e) {
            logger.warn("User data not found for request in {}", request.getRequestURI());

            HashMap<String, Object> map = new HashMap<>();
            map.put(ApiResponseKey.STATUS, RedirectAttributesKey.FAILED);
            map.put(ApiResponseKey.STATUS_CODE, 404);
            map.put(ApiResponseKey.MESSAGE, "User data not found");
            map.put(ApiResponseKey.DATA, null);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(map);
        } catch (Exception e) {
            logger.error("Internal server error in {}", request.getRequestURI());

            HashMap<String, Object> res = new HashMap<>();
            res.put(ApiResponseKey.STATUS, RedirectAttributesKey.FAILED);
            res.put(ApiResponseKey.STATUS_CODE, 500);
            res.put(ApiResponseKey.MESSAGE, "There's something wrong on the server");
            res.put(ApiResponseKey.DATA, null);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(res);
        }
    }
}
