package com.tugaskelompok.rumahsehat.appointment.restcontroller;

import com.tugaskelompok.rumahsehat.appointment.data_transfer_object.*;
import com.tugaskelompok.rumahsehat.config.auth.JwtTokenUtils;
import com.tugaskelompok.rumahsehat.config.ApiResponseKey;
import com.tugaskelompok.rumahsehat.config.RedirectAttributesKey;
import com.tugaskelompok.rumahsehat.pasien.model.PasienModel;
import com.tugaskelompok.rumahsehat.pasien.service.PasienService;
import com.tugaskelompok.rumahsehat.dokter.model.DokterModel;
import com.tugaskelompok.rumahsehat.dokter.service.DokterService;
import com.tugaskelompok.rumahsehat.appointment.model.AppointmentModel;
import com.tugaskelompok.rumahsehat.appointment.service.AppointmentService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.validation.BindingResult;
import org.springframework.context.MessageSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.web.firewall.RequestRejectedException;
import org.springframework.web.bind.annotation.*;
import org.springframework.validation.Errors;
import javax.validation.Valid;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

@RestController
@RequestMapping("api/v1/appointment")
public class AppointmentRestController {
    @Qualifier("appointmentServiceImpl")
    @Autowired
    AppointmentService appointmentService;

    @Autowired
    JwtTokenUtils jwtTokenUtils;

    @Autowired
    PasienService pasienService;

    @Autowired
    DokterService dokterService;

    @Autowired
    private MessageSource messageSource;

    private Logger logger = LoggerFactory.getLogger(AppointmentRestController.class);

    @PostMapping(value = "/add")
    public ResponseEntity<HashMap<String, Object>> addAppointment(HttpServletRequest request, @Valid @RequestBody AppointmentDTO appointmentDTO, Errors
            errors, BindingResult bindingResult) {
        try {
            if (errors.hasErrors()) {
                throw new RequestRejectedException("Request body missing");
            }

            final String requestTokenHeader = request.getHeader("Authorization");
            final var jwtToken = requestTokenHeader.substring(7);

            String username = jwtTokenUtils.getUsernameFromToken(jwtToken);
            var newAppointment = new AppointmentModel();
            PasienModel regPatient = pasienService.getPasienByUsername(username);
            DokterModel regDokter = dokterService.getDokterbyId(appointmentDTO.getDokterId());

            //setting default values
            newAppointment.setDokter(regDokter);
            newAppointment.setPasien(regPatient);
            newAppointment.setWaktuAwal(appointmentDTO.getWaktuAwal());
            newAppointment.setIsDone(false);

            //response HashMap
            HashMap<String, Object> res = new HashMap<>();

            //handling overlapping constraint
            List<AppointmentModel> listAppointmentDokter = regDokter.getListAppointment();
            for(AppointmentModel extAppointment : listAppointmentDokter){
                LocalDateTime extTime = extAppointment.getWaktuAwal();
                Long diffTime = ChronoUnit.HOURS.between(newAppointment.getWaktuAwal(), extTime);
                if(diffTime > -1.0 && diffTime < 1.0){
                    res.put(ApiResponseKey.STATUS, RedirectAttributesKey.FAILED);
                    res.put(ApiResponseKey.STATUS_CODE, 400);
                    res.put(ApiResponseKey.MESSAGE, "Waktu appointment tidak tersedia.");
                    res.put(ApiResponseKey.DATA, null);
                    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(res);
                }
            }

            appointmentService.createAppointment(newAppointment);
            res.put(ApiResponseKey.STATUS, RedirectAttributesKey.SUCCESS);
            res.put(ApiResponseKey.STATUS_CODE, 200);
            res.put(ApiResponseKey.MESSAGE, "Appointment has been created");

            HashMap<String, Object> resData = new HashMap<>();
            resData.put("pasien", newAppointment.getPasien().getNama());
            resData.put("dokter", newAppointment.getDokter().getNama());
            resData.put("waktu", newAppointment.getWaktuAwal());

            res.put(ApiResponseKey.DATA, resData);

            return ResponseEntity.ok(res);
        }
        catch (RequestRejectedException e) {
            var error = bindingResult.getFieldError("nominal");
            var errorMessage = "";

            if (error != null) {
                errorMessage = messageSource.getMessage(error, Locale.getDefault());
            } else {
                errorMessage = "There's an request body error";
            }

            logger.warn("Request rejected in Appointment API Request: {}", errorMessage);

            HashMap<String, Object> res = new HashMap<>();

            res.put(ApiResponseKey.STATUS, RedirectAttributesKey.FAILED);
            res.put(ApiResponseKey.STATUS_CODE, 400);
            res.put(ApiResponseKey.MESSAGE, errorMessage);
            res.put(ApiResponseKey.DATA, null);

            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(res); }
        catch (Exception e) {
            logger.error("There is an internal server error in {}", request.getRequestURI());

            HashMap<String, Object> res = new HashMap<>();
            res.put(ApiResponseKey.STATUS, RedirectAttributesKey.FAILED);
            res.put(ApiResponseKey.STATUS_CODE, 500);
            res.put(ApiResponseKey.MESSAGE, "Something wrong on the server");
            res.put(ApiResponseKey.DATA, null);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(res);
        }
    }

    @GetMapping(value = "/list-dokter")
    public List<DokterModel> retrieveListDokter() {
        return dokterService.getListDokter();
    }

    @GetMapping(value = "/detail/{kode}")
    public ResponseEntity<HashMap<String, Object>> detailAppointment(@PathVariable("kode") String kode, HttpServletRequest request) {
        try {

            AppointmentModel appointment = appointmentService.getAppointmentByKode(kode);

            HashMap<String, Object> res = new HashMap<>();
            res.put(ApiResponseKey.STATUS, RedirectAttributesKey.SUCCESS);
            res.put(ApiResponseKey.STATUS_CODE, 200);

            res.put("kode", appointment.getKode());
            res.put("waktuAwal", appointment.getWaktuAwal());
            res.put("statusAppointment", appointment.getIsDone());
            res.put("dokter", appointment.getDokter().getNama());
            res.put("pasien", appointment.getPasien().getNama());

            if (appointment.getResep() != null) {
                res.put("resep", appointment.getResep().getId());
            } else {
                res.put("resep", null);
            }

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

    @GetMapping(value = "")
    public ResponseEntity<HashMap<String, Object>> retrieveListAppointment(HttpServletRequest request) {
        try {
            final String requestTokenHeader = request.getHeader("Authorization");
            final var jwtToken = requestTokenHeader.substring(7);
            String username = jwtTokenUtils.getUsernameFromToken(jwtToken);

            HashMap<String, Object> map = new HashMap<>();
            if(!username.isEmpty()) {
                List<AppointmentModel> listAppointment = appointmentService.getListAppointmentByPasienUsername(username);
                map.put(ApiResponseKey.STATUS, RedirectAttributesKey.SUCCESS);
                map.put(ApiResponseKey.STATUS_CODE, 200);
                map.put(ApiResponseKey.MESSAGE, "List appointment successfully retrieved");
                map.put(ApiResponseKey.DATA, listAppointment);
            }
            else{
                logger.warn("Unauthorized pasien access to list appointment at {}", request.getRequestURI());
                map.put(ApiResponseKey.STATUS, RedirectAttributesKey.FAILED);
                map.put(ApiResponseKey.STATUS_CODE, 403);
                map.put(ApiResponseKey.MESSAGE, "Anda tidak terautorisasi sebagai pasien");
                map.put(ApiResponseKey.DATA, new ArrayList<>());
            }
            return ResponseEntity.ok(map);
        } catch (Exception e) {
            logger.error("Internal server error in {}", request.getRequestURI());

            HashMap<String, Object> map = new HashMap<>();
            map.put(ApiResponseKey.STATUS, RedirectAttributesKey.FAILED);
            map.put(ApiResponseKey.STATUS_CODE, 500);
            map.put(ApiResponseKey.MESSAGE, e.getMessage());
            map.put(ApiResponseKey.DATA, new ArrayList<>());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(map);
        }
    }

}
