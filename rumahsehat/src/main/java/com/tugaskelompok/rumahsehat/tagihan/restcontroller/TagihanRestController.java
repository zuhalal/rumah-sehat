package com.tugaskelompok.rumahsehat.tagihan.restcontroller;

import com.tugaskelompok.rumahsehat.tagihan.data_transfer_object.*;
import com.tugaskelompok.rumahsehat.config.auth.JwtTokenUtils;
import com.tugaskelompok.rumahsehat.pasien.model.PasienModel;
import com.tugaskelompok.rumahsehat.pasien.service.PasienService;
import com.tugaskelompok.rumahsehat.tagihan.model.TagihanModel;
import com.tugaskelompok.rumahsehat.tagihan.service.TagihanService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.validation.Errors;
import javax.validation.Valid;
import org.springframework.http.HttpHeaders;
import org.springframework.security.web.firewall.RequestRejectedException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.tugaskelompok.rumahsehat.config.ApiResponseKey;
import com.tugaskelompok.rumahsehat.config.RedirectAttributesKey;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.time.LocalDateTime;

@RestController
@RequestMapping("api/v1/tagihan")
public class TagihanRestController {
    @Qualifier("tagihanServiceImpl")
    @Autowired
    TagihanService tagihanService;

    @Autowired
    JwtTokenUtils jwtTokenUtils;

    @Autowired
    PasienService pasienService;

    private Logger logger = LoggerFactory.getLogger(TagihanRestController.class);

    @GetMapping(value = "/{kode}")
    public ResponseEntity<Map<String, Object>> getPasienDetailTagihan(@PathVariable String kode, HttpServletRequest request) {
        try {

            var now  = LocalDateTime.now().toString();

            logger.info("API Request in {} at {}", request.getRequestURI(), now);

            final String requestTokenHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
            final var jwtToken = requestTokenHeader.substring(7);

            String username = jwtTokenUtils.getUsernameFromToken(jwtToken);

            Map<String, Object> map = new HashMap<>();

            TagihanModel tagihan = tagihanService.getByKode(kode);

            if (tagihan == null) {
                map.put(ApiResponseKey.STATUS, RedirectAttributesKey.FAILED);
                map.put(ApiResponseKey.STATUS_CODE, 400);
                map.put(ApiResponseKey.MESSAGE, "Tagihan data not found");
                map.put(ApiResponseKey.DATA, null);
                logger.info("400 BAD request in {}", request.getRequestURI());
                return ResponseEntity.status(400).body(map);
            }

            if (!tagihan.getKodeAppointment().getPasien().getUsername().equals(username)) {
                map.put(ApiResponseKey.STATUS, RedirectAttributesKey.FAILED);
                map.put(ApiResponseKey.STATUS_CODE, 401);
                map.put(ApiResponseKey.MESSAGE, "Access Denied");
                map.put(ApiResponseKey.DATA, null);
                logger.info("401 Unauthorized request in {}", request.getRequestURI());
                return ResponseEntity.status(401).body(map);
            }

            map.put(ApiResponseKey.STATUS, RedirectAttributesKey.SUCCESS);
            map.put(ApiResponseKey.STATUS_CODE, 200);
            map.put(ApiResponseKey.MESSAGE, "Tagihan pasien successfully retrieved");
            map.put(ApiResponseKey.DATA, tagihan);
            logger.info("200 OK request in {}", request.getRequestURI());
            return ResponseEntity.ok(map);
        } catch (Exception e) {
            HashMap<String, Object> map = new HashMap<>();
            map.put(ApiResponseKey.STATUS, RedirectAttributesKey.FAILED);
            map.put(ApiResponseKey.STATUS_CODE, 500);
            map.put(ApiResponseKey.MESSAGE, e.getMessage());
            map.put(ApiResponseKey.DATA, null);
            logger.error("500 Internal server error request in {}", request.getRequestURI());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(map);
        }
    }

    @PostMapping(value = "/pembayaran")
    public ResponseEntity<Map<String, Object>> topUpSaldo(HttpServletRequest request, @Valid @RequestBody TagihanPembayaranRequest tagihanPembayaranRequest, Errors
                                         errors) {
        try {
            if (errors.hasErrors()) {
                throw new RequestRejectedException("Saldo tidak cukup");
            }

            final String requestTokenHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
            final var jwtToken = requestTokenHeader.substring(7);

            String username = jwtTokenUtils.getUsernameFromToken(jwtToken);

            PasienModel pasien = pasienService.getPasienByUsername(username);

            TagihanModel tagihan = tagihanService.getByKode(tagihanPembayaranRequest.getKode());

            HashMap<String, Object> res = new HashMap<>();
            if(pasien.getSaldo() < tagihan.getJumlahTagihan()){
                res.put(ApiResponseKey.STATUS, RedirectAttributesKey.FAILED);
                res.put(ApiResponseKey.STATUS_CODE, 400);
                res.put(ApiResponseKey.MESSAGE, "saldo tidak mencukupi");
                res.put(ApiResponseKey.DATA, null);
                logger.info("400 BAD request in {}", request.getRequestURI());
                return ResponseEntity.ok(res);
            }

            TagihanModel updatedTagihan = tagihanService.pembayaran(tagihan.getKode(), username);

            res.put(ApiResponseKey.STATUS, "success");
            res.put(ApiResponseKey.STATUS_CODE, 200);
            res.put(ApiResponseKey.MESSAGE, "Pembayaran Berhasil");
            res.put(ApiResponseKey.DATA, updatedTagihan);

            logger.info("200 OK request in {}", request.getRequestURI());

            HashMap<String, Object> resData = new HashMap<>();
            resData.put("isPaid", updatedTagihan.getIsPaid());
            resData.put("kode", updatedTagihan.getKode());

            res.put(ApiResponseKey.DATA, resData);

            return ResponseEntity.ok(res);
        } catch (Exception e) {
            HashMap<String, Object> res = new HashMap<>();
            res.put(ApiResponseKey.STATUS, RedirectAttributesKey.FAILED);
            res.put(ApiResponseKey.STATUS_CODE, 500);
            res.put(ApiResponseKey.MESSAGE, "There's something wrong on the server");
            res.put(ApiResponseKey.DATA, null);
            logger.error("500 Internal server error request in {}", request.getRequestURI());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(res);
        }
    }

    @GetMapping(value = "")
    public ResponseEntity<HashMap<String, Object>> retrieveListTagihan(HttpServletRequest request) { //
        try {
            final String requestTokenHeader = request.getHeader("Authorization");
            final var jwtToken = requestTokenHeader.substring(7); //declare with var
            String username = jwtTokenUtils.getUsernameFromToken(jwtToken);

            HashMap<String, Object> map = new HashMap<>();
            if(!username.isEmpty()) { //clean code: Define a constant
                List<TagihanModel> listTagihan = tagihanService.getListTagihanByPasienUsername(username);
                map.put(ApiResponseKey.STATUS, "success");
                map.put(ApiResponseKey.STATUS_CODE, 200);
                map.put(ApiResponseKey.MESSAGE, "List tagihan successfully retrieved");
                map.put(ApiResponseKey.DATA, listTagihan);
            }
            else{ //clean code: Define a constant
                logger.warn("Unauthorized pasien access to list tagihan at {}", request.getRequestURI());

                map.put(ApiResponseKey.STATUS, "failed");
                map.put(ApiResponseKey.STATUS_CODE, 403);
                map.put(ApiResponseKey.MESSAGE, "Anda tidak terautorisasi sebagai pasien");
                map.put(ApiResponseKey.DATA, new ArrayList<>());
            }
            return ResponseEntity.ok(map);
        } catch (Exception e) { //clean code: Define a constant
            logger.error("Internal server error in {}", request.getRequestURI());

            HashMap<String, Object> map = new HashMap<>(); //clean code: Replace the type specification in this constructor call with the diamond operator ("<>").
            map.put(ApiResponseKey.STATUS, "failed");
            map.put(ApiResponseKey.STATUS_CODE, 500);
            map.put(ApiResponseKey.MESSAGE, e.getMessage());
            map.put(ApiResponseKey.DATA, new ArrayList<>());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(map);
        }
    }
}
