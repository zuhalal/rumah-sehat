package com.tugaskelompok.rumahsehat.pasien.controller;

import com.tugaskelompok.rumahsehat.pasien.model.*;
import com.tugaskelompok.rumahsehat.pasien.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/pasien")
public class PasienController {
    @Qualifier("pasienServiceImpl")
    @Autowired
    private PasienService pasienService;

    @GetMapping("/viewall")
    public String listDokter (Model model) {
        List<PasienModel> listPasien = pasienService.getListPasien();
        model.addAttribute("listPasien", listPasien);
        return "pasien/view-all-pasien";
    }
}

