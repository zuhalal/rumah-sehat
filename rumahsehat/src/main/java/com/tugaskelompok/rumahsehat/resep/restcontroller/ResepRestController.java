package com.tugaskelompok.rumahsehat.resep.restcontroller;

import com.tugaskelompok.rumahsehat.config.ApiResponseKey;
import com.tugaskelompok.rumahsehat.config.RedirectAttributesKey;
import com.tugaskelompok.rumahsehat.config.auth.JwtTokenUtils;
import com.tugaskelompok.rumahsehat.resep.model.ResepModel;
import com.tugaskelompok.rumahsehat.resep.service.ResepRestService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.nio.file.AccessDeniedException;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.NoSuchElementException;

@RestController
@RequestMapping("api/v1/resep")
public class ResepRestController {
    @Qualifier("resepRestServiceImpl")
    @Autowired
    ResepRestService resepRestService;

    @Autowired
    JwtTokenUtils jwtTokenUtils;

    private Logger logger = LoggerFactory.getLogger(ResepRestController.class);

    @GetMapping("/{id}")
    public ResponseEntity<Object> getPasienDetailResep(@PathVariable String id, HttpServletRequest request) {
        try {
            final String requestTokenHeader = request.getHeader("Authorization");
            final var jwtToken = requestTokenHeader.substring(7);

            String username = jwtTokenUtils.getUsernameFromToken(jwtToken);

            ResepModel resep = resepRestService.getById(id);

            if (!resep.getKodeAppointment().getPasien().getUsername().equals(username)) {
                throw new AccessDeniedException("Pasien dilarang mengakses resep detail ini");
            }

            HashMap<String, Object> map = new HashMap<>();

            map.put(ApiResponseKey.STATUS, "success");
            map.put(ApiResponseKey.STATUS_CODE, 200);
            map.put(ApiResponseKey.MESSAGE, "Resep pasien successfully retrieved");
            map.put(ApiResponseKey.DATA, resep);

            return ResponseEntity.ok(map);
        } catch (AccessDeniedException e) {
            var now = LocalDateTime.now();
            logger.warn("Unauthorized pasien access to detail resep at {}", now);

            HashMap<String, Object> map = new HashMap<>();
            map.put(ApiResponseKey.STATUS, RedirectAttributesKey.FAILED);
            map.put(ApiResponseKey.STATUS_CODE, 401);
            map.put(ApiResponseKey.MESSAGE, "Access Denied");
            map.put(ApiResponseKey.DATA, null);

            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(map);
        }  catch (NoSuchElementException e) {
            logger.error("Resep detail API request with id resep {} is not found", id);
            
            HashMap<String, Object> map = new HashMap<>();
            map.put(ApiResponseKey.STATUS, "failed");
            map.put(ApiResponseKey.STATUS_CODE, 404);
            map.put(ApiResponseKey.MESSAGE, "Resep data not found");
            map.put(ApiResponseKey.MESSAGE, null);

            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(map);
        } catch (Exception e) {
            logger.error("Internal server error in {}", request.getRequestURI());

            HashMap<String, Object> map = new HashMap<>();
            map.put(ApiResponseKey.STATUS, "failed");
            map.put(ApiResponseKey.STATUS_CODE, 500);
            map.put(ApiResponseKey.MESSAGE, e.getMessage());
            map.put(ApiResponseKey.DATA, null);
            
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(map);
        }
    }
}
