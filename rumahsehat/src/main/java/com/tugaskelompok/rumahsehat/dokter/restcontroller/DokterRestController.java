package com.tugaskelompok.rumahsehat.dokter.restcontroller;
import com.tugaskelompok.rumahsehat.config.ApiResponseKey;
import com.tugaskelompok.rumahsehat.config.RedirectAttributesKey;
import com.tugaskelompok.rumahsehat.config.auth.JwtTokenUtils;
import com.tugaskelompok.rumahsehat.dokter.model.DokterModel;
import com.tugaskelompok.rumahsehat.dokter.service.DokterService;
import com.tugaskelompok.rumahsehat.pasien.service.PasienService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;

@RestController
@RequestMapping("api/v1/dokter")
public class DokterRestController {
    @Qualifier("dokterServiceImpl")
    @Autowired
    DokterService dokterService;

    @Autowired
    JwtTokenUtils jwtTokenUtils;

    @Autowired
    PasienService pasienService;

    private Logger logger = LoggerFactory.getLogger(DokterRestController.class);

    @GetMapping(value = "/list-dokter")
    public ResponseEntity<HashMap<String, Object>> retrieveListDokter(HttpServletRequest request) {
        try {
            final String requestTokenHeader = request.getHeader("Authorization");
            final var jwtToken = requestTokenHeader.substring(7);
            String username = jwtTokenUtils.getUsernameFromToken(jwtToken);

            HashMap<String, Object> map = new HashMap<>();
            if(!username.isEmpty()) {
                List<DokterModel> listDokter = dokterService.getListDokter();

                map.put(ApiResponseKey.STATUS, RedirectAttributesKey.SUCCESS);
                map.put(ApiResponseKey.STATUS_CODE, 200);
                map.put(ApiResponseKey.MESSAGE, "List dokter successfully retrieved");
                map.put(ApiResponseKey.DATA, listDokter);
            }
            else{
                logger.warn("Unauthorized pasien access to list dokter at {}", request.getRequestURI());

                map.put(ApiResponseKey.STATUS, RedirectAttributesKey.FAILED);
                map.put(ApiResponseKey.STATUS_CODE, 403);
                map.put(ApiResponseKey.MESSAGE, "Anda tidak terautorisasi sebagai pasien");
                map.put(ApiResponseKey.DATA, null);
            }
            return ResponseEntity.ok(map);
        } catch (Exception e) {
            logger.error("Internal server error in {}", request.getRequestURI());

            HashMap<String, Object> map = new HashMap<>();

            map.put(ApiResponseKey.STATUS, RedirectAttributesKey.FAILED);
            map.put(ApiResponseKey.STATUS_CODE, 500);
            map.put(ApiResponseKey.MESSAGE, e.getMessage());
            map.put(ApiResponseKey.DATA, null);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(map);
        }
    }
}
