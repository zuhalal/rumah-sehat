package com.tugaskelompok.rumahsehat.resep.controller;

import com.tugaskelompok.rumahsehat.apoteker.model.ApotekerModel;
import com.tugaskelompok.rumahsehat.apoteker.service.ApotekerService;
import com.tugaskelompok.rumahsehat.appointment.model.AppointmentModel;
import com.tugaskelompok.rumahsehat.appointment.service.AppointmentService;
import com.tugaskelompok.rumahsehat.config.RedirectAttributesKey;
import com.tugaskelompok.rumahsehat.jumlah.model.JumlahModel;
import com.tugaskelompok.rumahsehat.jumlah.service.JumlahService;
import com.tugaskelompok.rumahsehat.obat.model.ObatModel;
import com.tugaskelompok.rumahsehat.obat.service.ObatService;
import com.tugaskelompok.rumahsehat.resep.data_transfer_object.ResepDTO;
import com.tugaskelompok.rumahsehat.resep.model.*;
import com.tugaskelompok.rumahsehat.resep.service.*;
import com.tugaskelompok.rumahsehat.tagihan.model.TagihanModel;
import com.tugaskelompok.rumahsehat.tagihan.service.TagihanService;
import com.tugaskelompok.rumahsehat.user.model.UserModel;
import com.tugaskelompok.rumahsehat.user.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.web.firewall.RequestRejectedException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.security.Principal;
import java.util.NoSuchElementException;

@Controller
@RequestMapping("/resep")
public class ResepController {

    @Qualifier("resepServiceImpl")
    @Autowired
    private ResepService resepService;

    @Qualifier("apotekerServiceImpl")
    @Autowired
    private ApotekerService apotekerService;

    @Qualifier("appointmentServiceImpl")
    @Autowired
    private AppointmentService appointmentService;

    @Qualifier("obatServiceImpl")
    @Autowired
    private ObatService obatService;

    @Qualifier("tagihanServiceImpl")
    @Autowired
    private TagihanService tagihanService;

    @Qualifier("userServiceImpl")
    @Autowired
    private UserService userService;

    @Qualifier("jumlahServiceImpl")
    @Autowired
    private JumlahService jumlahService;

    private Logger logger = LoggerFactory.getLogger(ResepController.class);
    private static final String RESEP = "resep";
    private static final String LISTOBATEXISTING = "listObatExisting";
    private static final String FORMADDRESEP = "resep/form-add-resep";

    //1
    @GetMapping(value = "/add/{kode}")
    public String addResepFormPage(@PathVariable String kode, Model model){
        var resep = new ResepDTO();
        List<ObatModel> listObat = obatService.getListObat();
        List<JumlahModel> listJumlahNew = new ArrayList<>();
        resep.setListJumlah(listJumlahNew);
        resep.getListJumlah().add(new JumlahModel());

        model.addAttribute("kode", kode);
        model.addAttribute(RESEP, resep);
        model.addAttribute(LISTOBATEXISTING, listObat);
        return FORMADDRESEP;
    }

    //2
    @PostMapping(value = "/add/{kode}")
    public String addResepSubmitPage(ResepDTO resepDTO, @PathVariable String kode){
        var resep = new ResepModel();
        resep.setListJumlah(resepDTO.getListJumlah());

        if(resepDTO.getListJumlah() == null){
            resepDTO.setListJumlah(new ArrayList<>());
        }

        else{
            var idx = 0;
            for(JumlahModel jumlah : resep.getListJumlah()){
                jumlah.setResep(resep);
                jumlah.setObat(resep.getListJumlah().get(idx).getObat());
                jumlah.setKuantitas(resep.getListJumlah().get(idx).getKuantitas());
                idx++;
            }
        }

        AppointmentModel appointment = appointmentService.getAppointmentByKode(kode);
        resep.setKodeAppointment(appointment);

       var now = LocalDateTime.now();
        resep.setCreatedAt(now);
        resep.setIsDone(Boolean.FALSE);
        resepService.addResep(resep);

        return "resep/viewall-resep";
    }

    //3
    @PostMapping(value = "/add/{kode}", params = {"addRowJumlah"})
    private String addRowJumlahMultiple(ResepDTO resep, Model model, @PathVariable String kode
    ){
        if(resep.getListJumlah() == null){
            resep.setListJumlah(new ArrayList<>());
        }
        resep.getListJumlah().add(new JumlahModel());

        List<ObatModel> listObat = obatService.getListObat();
        model.addAttribute("kode", kode);
        model.addAttribute(RESEP, resep);
        model.addAttribute(LISTOBATEXISTING, listObat);
        return FORMADDRESEP;
    }

    //4
    @PostMapping(value = "/add/{kode}", params = {"deleteRowJumlah"})
    private String deleteRowJumlahMultiple(
            ResepDTO resep,
            @RequestParam("deleteRowJumlah") Integer row,
            @PathVariable String kode,
            Model model
    ){
        if(resep.getListJumlah() == null){
        resep.setListJumlah(new ArrayList<>());
        }
        final Integer rowId = row;
        JumlahModel jumlah = resep.getListJumlah().get(rowId.intValue());
        resep.getListJumlah().remove(rowId.intValue());
        jumlahService.deleteJumlah(jumlah);

        List<ObatModel> listObat = obatService.getListObat();
        model.addAttribute("kode", kode);
        model.addAttribute(RESEP, resep);
        model.addAttribute(LISTOBATEXISTING, listObat);
        return FORMADDRESEP;
    }

    @GetMapping("/detail/{idResep}")
    public String resepDetailPage(@PathVariable String idResep, Model model, Principal principal) {
        try {
            ResepModel resep = resepService.getById(idResep);

            if (resep == null) {
                throw new NoSuchElementException("Resep tidak ditemukan");
            }

            UserModel userData = userService.getUserByUsername(principal.getName());

            model.addAttribute(RESEP, resep);
            model.addAttribute("role", userData.getRole().toString());
            return "resep/detail-resep";
        } catch (NoSuchElementException e) {
            logger.error("Resep dengan id resep {} tidak ditemukan", idResep);

            UserModel userData = userService.getUserByUsername(principal.getName());

            model.addAttribute(RedirectAttributesKey.ERROR, e.getMessage());
            model.addAttribute("role", userData.getRole().toString());
            return "resep/detail-resep";
        }
    }

    @PostMapping("/confirm/{id}")
    public ModelAndView resepConfirmation(@PathVariable String id, RedirectAttributes redirectAttrs, Principal principal) {
       try {
           ResepModel resep = resepService.getById(id);
           boolean isStokObatExist = resepService.checkStokObat(resep.getListJumlah());

           if (!isStokObatExist) {
               throw new RequestRejectedException("Stok obat tidak cukup untuk membuat resep ini");
           }

           ApotekerModel apoteker = apotekerService.getApotekerByUsername(principal.getName());

           if (apoteker == null) {
               throw new RequestRejectedException("Confirmer resep tidak valid");
           }

           ResepModel updatedResep = resepService.confirmResep(resep, apoteker);

           for (JumlahModel jumlah: resep.getListJumlah()) {
               obatService.calculateStock(jumlah.getObat(), jumlah.getKuantitas());
           }

           if (updatedResep.getIsDone()) {
               AppointmentModel appointment = appointmentService.updateAppointmentStatusToDone(resep.getKodeAppointment());

               if (appointment.getIsDone()) {
                   var tagihan = new TagihanModel();
                   tagihan.setTanggalTerbuat(LocalDateTime.now());
                   tagihan.setIsPaid(false);
                   Integer harga = obatService.getHargaFromListJumlahObat(resep.getListJumlah());
                   tagihan.setJumlahTagihan(resep.getKodeAppointment().getDokter().getTarif() + harga);
                   tagihan.setKodeAppointment(appointment);

                   tagihanService.addTagihan(tagihan);
               }
           } else {
               throw new RequestRejectedException("Gagal mengupdate resep");
           }

           redirectAttrs.addFlashAttribute(RedirectAttributesKey.SUCCESS, "Resep berhasil dikonfirmasi");
           return new ModelAndView("redirect:/resep/detail/" + id);
       }  catch (RequestRejectedException e) {
           logger.error(e.getMessage());
           redirectAttrs.addFlashAttribute("error", e.getMessage());
           return new ModelAndView("redirect:/resep/detail/" + id);
       }
    }

    @GetMapping("/viewall")
    public String listResep(Model model) {
        List<ResepModel> listResep = resepService.getListResep();
        model.addAttribute("listResep", listResep);
        return "resep/viewall-resep";
    }
}
