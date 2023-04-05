package com.tugaskelompok.rumahsehat.apoteker.controller;

import com.tugaskelompok.rumahsehat.apoteker.data_transfer_object.*;
import com.tugaskelompok.rumahsehat.apoteker.model.*;
import com.tugaskelompok.rumahsehat.apoteker.service.*;
import com.tugaskelompok.rumahsehat.user.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/apoteker")
public class ApotekerController {
    @Qualifier("apotekerServiceImpl")
    @Autowired
    private ApotekerService apotekerService;

    @Autowired
    private UserService userService;

    @GetMapping("/viewall")
    public String listApoteker (Model model) {
        List<ApotekerModel> listApoteker = apotekerService.getListApoteker();
        model.addAttribute("listApoteker", listApoteker);
        return "apoteker/view-all-apoteker";
    }

    @GetMapping("/add")
    public String addApotekerFormPage(Model model) {
        var apoteker = new ApotekerRequestDTO();
        model.addAttribute("apoteker", apoteker);
        return "apoteker/form-add-apoteker";
    }

    @PostMapping(value = "/add", params = {"save"})
    public String addApotekerSubmitPage (@ModelAttribute ApotekerRequestDTO apoteker, Model model, RedirectAttributes redirectAttrs) {
        var attributeName = "";
        var attributeValue = "";

        if(userService.getUserByUsername(apoteker.getUsername()) != null){
            attributeValue = "Username already exist";
        }
        if(userService.getUserByEmail(apoteker.getEmail()) != null){
            attributeValue = "Email already exist";
        }
        if(!userService.checkPassword(apoteker.getPassword()).equals("correct")){
            attributeValue = userService.checkPassword(apoteker.getPassword());
        }

        if(!attributeValue.equals("")){
            attributeName = "error";
            redirectAttrs.addFlashAttribute(attributeName, attributeValue);
            return "redirect:/apoteker/add";
        }
        
        apotekerService.addApoteker(apoteker);
        redirectAttrs.addFlashAttribute("success", "Apoteker berhasil ditambahkan");
        return "redirect:/apoteker/viewall";
        
    }
}

