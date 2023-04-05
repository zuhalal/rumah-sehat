package com.tugaskelompok.rumahsehat.dokter.controller;

import com.tugaskelompok.rumahsehat.dokter.data_transfer_object.*;
import com.tugaskelompok.rumahsehat.dokter.model.*;
import com.tugaskelompok.rumahsehat.dokter.service.*;
import com.tugaskelompok.rumahsehat.user.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/dokter")
public class DokterController {
    @Qualifier("dokterServiceImpl")
    @Autowired
    private DokterService dokterService;

    @Autowired
    private UserService userService;

    @GetMapping("/viewall")
    public String listDokter (Model model) {
        List<DokterModel> listDokter = dokterService.getListDokter();
        model.addAttribute("listDokter", listDokter);
        return "dokter/view-all-dokter";
    }

    @GetMapping("/add")
    public String addDokterFormPage(Model model) {
        var  dokter = new DokterRequestDTO();
        model.addAttribute("dokter", dokter);
        return "dokter/form-add-dokter";
    }

    @PostMapping(value = "/add", params = {"save"})
    public String addDokterSubmitPage (@ModelAttribute DokterRequestDTO dokter, Model model, RedirectAttributes redirectAttrs) {
    
        var attributeName = "";
        var attributeValue = "";

        if(userService.getUserByUsername(dokter.getUsername()) != null){
            attributeValue = "Username already exist";

        }
        if(userService.getUserByEmail(dokter.getEmail()) != null){
            attributeValue = "Email already exist";
        }
        if(!userService.checkPassword(dokter.getPassword()).equals("correct")){
            attributeValue = userService.checkPassword(dokter.getPassword());
        }
        
        if(!attributeValue.equals("")){
            attributeName = "error";
            redirectAttrs.addFlashAttribute(attributeName, attributeValue);
            return "redirect:/dokter/add";
        }

        dokterService.addDokter(dokter);
        redirectAttrs.addFlashAttribute("success", "Dokter berhasil ditambahkan");
        return "redirect:/dokter/viewall";
        
    }
}

