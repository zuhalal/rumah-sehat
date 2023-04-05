package com.tugaskelompok.rumahsehat.obat.controller;

import com.tugaskelompok.rumahsehat.config.ApiResponseKey;
import com.tugaskelompok.rumahsehat.config.RedirectAttributesKey;
import com.tugaskelompok.rumahsehat.obat.data_transfer_object.DailyObatDTO;
import com.tugaskelompok.rumahsehat.obat.data_transfer_object.ObatDTO;
import com.tugaskelompok.rumahsehat.obat.data_transfer_object.PeriodicObatResponseDTO;
import com.tugaskelompok.rumahsehat.obat.projections.MonthlyObatIncomeProjections;
import com.tugaskelompok.rumahsehat.obat.projections.ObatKuantitasProjections;
import com.tugaskelompok.rumahsehat.obat.projections.ObatTotalPendapatanProjections;
import com.tugaskelompok.rumahsehat.obat.model.ObatModel;
import com.tugaskelompok.rumahsehat.obat.service.ObatService;
import com.tugaskelompok.rumahsehat.user.model.UserModel;
import com.tugaskelompok.rumahsehat.user.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import java.security.Principal;
import java.util.*;

@Controller
@RequestMapping("/obat")
public class ObatController {

    @Qualifier("obatServiceImpl")
    @Autowired
    private ObatService obatService;

    @Qualifier("userServiceImpl")
    @Autowired
    private UserService userService;

    private Logger logger = LoggerFactory.getLogger(ObatController.class);
    private static final String LISTOBAT = "listObat";
    @GetMapping("/viewall")
    public String listObat (Model model, Principal principal) {
        List<ObatModel> listObat = obatService.getListObat();
        UserModel userData = userService.getUserByUsername(principal.getName());

        model.addAttribute(LISTOBAT, listObat);
        model.addAttribute("role", userData.getRole().toString());
        return "obat/view-all-obat";
    }

    @GetMapping("/update/{id}")
    public String updateObatFormPage(@PathVariable String id, Model model) {
        var authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication.isAuthenticated() && authentication.getAuthorities().stream().anyMatch(a -> a.getAuthority().equalsIgnoreCase("APOTEKER"))) {
            ObatModel obatAwal = obatService.getObatById(id);
            var obat = new ObatDTO();
            obat.setIdObat(obatAwal.getIdObat());
            obat.setNamaObat(obatAwal.getNamaObat());
            obat.setStok(obatAwal.getStok());
            model.addAttribute("obat", obat);
            return "obat/form-update-obat";
        }

        return "error/403";
    }

    @PostMapping(value="/update/{id}", params = {"save"})
    public String updateObatSubmitPage(ObatDTO obat, @PathVariable String id, RedirectAttributes redirectAttrs) {
        ObatModel obatObject = obatService.getObatById(id);
        obatService.updateStokObat(obatObject, obat.getStok());
        redirectAttrs.addFlashAttribute("success", "Stok obat berhasil diubah");
        return "redirect:/obat/viewall";
    }

    @GetMapping("/chart")
    public String obatPeriodic () {
        return "obat/chart-obat-periodic";
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping("/chart/data")
    public ResponseEntity<Map<String, Object>> periodicChart(HttpServletRequest request) {
        try {
            List<MonthlyObatIncomeProjections> monthlyObatIncomeResponses = obatService.getAllMonthlyIncomeThisYear();

            List<PeriodicObatResponseDTO> finalData = obatService.fillMissingMonthsAndSortMonths(monthlyObatIncomeResponses);

            Map<String, Object> map = new HashMap<>();

            map.put(ApiResponseKey.STATUS, "success");
            map.put(ApiResponseKey.STATUS_CODE, 200);
            map.put(ApiResponseKey.MESSAGE, "Statistic of Obat Periodically succesfully retrieved");
            map.put(ApiResponseKey.DATA, finalData);

            return ResponseEntity.ok(map);
        } catch(Exception e) {
            logger.error("Exception error in {}", request.getRequestURI());
            logger.error("Exception message: {}", e.getMessage());

            Map<String, Object> map = new HashMap<>();

            map.put(ApiResponseKey.STATUS, "failed");
            map.put(ApiResponseKey.STATUS_CODE, 500);
            map.put(ApiResponseKey.MESSAGE, e.getMessage());
            map.put(ApiResponseKey.DATA, null);

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(map);
        }
    }

    @GetMapping("/barchart-obat")
    public String obatBarchart (Model model) {
        List<ObatModel> listObat = obatService.getListObat();
        model.addAttribute(LISTOBAT, listObat);
        return "obat/barchart-obat";
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping(value = "/chart/barchart/{obat}/{tipe}")
    public ResponseEntity<Map<String, Object>> barchartObatView(@PathVariable String obat, @PathVariable String tipe) {
        Map<String, Object> map = new HashMap<>();
        obat = obat.replace("=", "/");
        List<String> daftarObat = new ArrayList<>(Arrays.asList(obat.split("!")));
        
        if(tipe.equals("Kuantitas Penjualan")){
            List<ObatKuantitasProjections> data = obatService.getAllObatKuantitasProjections(daftarObat);
            map.put(ApiResponseKey.STATUS, RedirectAttributesKey.SUCCESS);
            map.put(ApiResponseKey.STATUS_CODE, 200);
            map.put(ApiResponseKey.MESSAGE, "Statistic of Kuantitas Penjualan Obat succesfully retrieved");
            map.put(ApiResponseKey.DATA, data);
            map.put("tipe", "Kuantitas Penjualan");
        }

        if(tipe.equals("Total Pendapatan")){
            List<ObatTotalPendapatanProjections> data = obatService.getAllObatTotalPendapatanProjections(daftarObat);
            map.put(ApiResponseKey.STATUS, RedirectAttributesKey.SUCCESS);
            map.put(ApiResponseKey.STATUS_CODE, 200);
            map.put(ApiResponseKey.MESSAGE, "Statistic of Obat Periodically succesfully retrieved");
            map.put(ApiResponseKey.DATA, data);
            map.put("tipe", "Total Pendapatan");
        }

        return ResponseEntity.ok(map);
    }

    @GetMapping("/linechart-obat")
    public String lineChartObatView(Model model) {
        List<ObatModel> listObat = obatService.getListObat();
        model.addAttribute(LISTOBAT, listObat);
        return "obat/line-chart-obat";
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping(value = "chart/linechart/{obat}/{tipe}")
    public ResponseEntity<Map<String, Object>> lineChartsView(@PathVariable String obat, @PathVariable(name = "tipe") String tipe) {
        Map<String, Object> map = new HashMap<>();
        List<String> daftarObat = new ArrayList<>(Arrays.asList(obat.split("!")));
        if(tipe.equals("Tahun")){
            List<List<PeriodicObatResponseDTO>> listOfFinalData = new ArrayList<>();
            for(String idObat : daftarObat){
                listOfFinalData.add(obatService.lineCharts(idObat));
            }
            map.put(ApiResponseKey.STATUS, RedirectAttributesKey.SUCCESS);
            map.put(ApiResponseKey.STATUS_CODE, 200);
            map.put(ApiResponseKey.MESSAGE, "Statistic of Penjualan Obat Tahunan succesfully retrieved");
            map.put(ApiResponseKey.DATA, listOfFinalData);
            map.put("tipe", "Penjualan Tahunan");
        }

        if(tipe.equals("Bulan")){
            List<List<DailyObatDTO>> listOfFinalData = new ArrayList<>();
            for(String idObat : daftarObat){
                listOfFinalData.add(obatService.lineChartsDaily(idObat));
            }
            map.put(ApiResponseKey.STATUS, RedirectAttributesKey.SUCCESS);
            map.put(ApiResponseKey.STATUS_CODE, 200);
            map.put(ApiResponseKey.MESSAGE, "Statistic of Penjualan Obat Bulanan succesfully retrieved");
            map.put(ApiResponseKey.DATA, listOfFinalData);
            map.put("tipe", "Penjualan Bulanan");
        }

        return ResponseEntity.ok(map);
    }
}
