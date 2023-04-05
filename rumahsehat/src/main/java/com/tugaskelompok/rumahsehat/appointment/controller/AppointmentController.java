package com.tugaskelompok.rumahsehat.appointment.controller;

import com.tugaskelompok.rumahsehat.appointment.model.AppointmentModel;
import com.tugaskelompok.rumahsehat.appointment.service.AppointmentService;
import com.tugaskelompok.rumahsehat.resep.model.ResepModel;
import com.tugaskelompok.rumahsehat.resep.service.ResepService;
import com.tugaskelompok.rumahsehat.tagihan.model.TagihanModel;
import com.tugaskelompok.rumahsehat.tagihan.service.TagihanService;
import com.tugaskelompok.rumahsehat.user.model.UserModel;
import com.tugaskelompok.rumahsehat.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;
import java.util.List;

@Controller
@RequestMapping("/appointment")
public class AppointmentController {
    @Qualifier("appointmentServiceImpl")
    @Autowired
    private AppointmentService appointmentService;

    @Qualifier("tagihanServiceImpl")
    @Autowired
    private TagihanService tagihanService;

    @Qualifier("userServiceImpl")
    @Autowired
    private UserService userService;

    @Qualifier("resepServiceImpl")
    @Autowired
    private ResepService resepService;

    private String redirectDetailAppointment = "appointment/detail-appointment";
    private String detailAppointment = "appointment";

    @GetMapping("/viewall")
    public String listAppointment(Model model, Principal principal) {
        List<AppointmentModel> listAppointment = appointmentService.getListAppointment();

        var user = userService.getUserByUsername(principal.getName());

        if (user.getRole().toString().equals("DOKTER")) {
            listAppointment = appointmentService.getListAppointmentByDokterUsername(String.valueOf(user.getUsername()));
        }

        model.addAttribute("listAppointment", listAppointment);
        return "appointment/viewall-appointment";
    }

    @GetMapping("/detail/{kode}")
    public String appointmentDetailPage(@PathVariable String kode, Model model, Principal principal) {
        AppointmentModel appointment = appointmentService.getAppointmentByKode(kode);

        UserModel userData = userService.getUserByUsername(principal.getName());

        ResepModel resep = resepService.getByKodeAppointment(kode);

        if (resep != null) {
            model.addAttribute("resep", resep);
        }

        model.addAttribute(detailAppointment, appointment);
        model.addAttribute("role", userData.getRole().toString());
        return redirectDetailAppointment;
    }

    @PostMapping(value="/detail/{kode}", params = {"save"})
    public String appointmentUpdateStatus(@PathVariable String kode, Model model, RedirectAttributes redirectAttrs) {
        AppointmentModel appointment = appointmentService.getAppointmentByKode(kode);
        var authentication = SecurityContextHolder.getContext().getAuthentication();
        if (appointment.getResep() != null
                && appointment.getResep().getConfirmerUUID() == null
                && authentication.isAuthenticated()
                && authentication.getAuthorities().stream().anyMatch(a -> a.getAuthority().equalsIgnoreCase("DOKTER"))) {
            model.addAttribute(detailAppointment, appointment);
            redirectAttrs.addFlashAttribute("error", "Dokter tidak dapat menyelesaikan appointment");
            return redirectDetailAppointment;
        }
        appointmentService.updateStatusAppointment(appointment);
        var tagihan = new TagihanModel();
        tagihanService.addTagihan(tagihan,appointment);
        model.addAttribute(detailAppointment, appointment);
        redirectAttrs.addFlashAttribute("success", "Tagihan berhasil dibuat");
        return redirectDetailAppointment;
    }

}
