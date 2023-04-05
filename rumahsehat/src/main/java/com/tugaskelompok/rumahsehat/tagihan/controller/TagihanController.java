package com.tugaskelompok.rumahsehat.tagihan.controller;

import com.tugaskelompok.rumahsehat.appointment.service.AppointmentService;
import com.tugaskelompok.rumahsehat.tagihan.service.TagihanService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/tagihan")
public class TagihanController {
    @Qualifier("tagihanServiceImpl")
    @Autowired
    private TagihanService tagihanService;

    @Qualifier("appointmentServiceImpl")
    @Autowired
    private AppointmentService appointmentService;
}
