package com.tugaskelompok.rumahsehat.appointment.service;

import com.tugaskelompok.rumahsehat.appointment.model.AppointmentModel;
import com.tugaskelompok.rumahsehat.appointment.repository.AppointmentDb;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class AppointmentServiceImpl implements AppointmentService {
    @Autowired
    AppointmentDb appointmentDb;

    @Override
    public AppointmentModel getAppointmentByKode(String kode) {
        Optional<AppointmentModel> appointment = appointmentDb.findByKode(kode);
        return appointment.orElse(null);
    }

    @Override
    public AppointmentModel updateAppointmentStatusToDone(AppointmentModel appointment) {
        appointment.setIsDone(true);
        appointmentDb.save(appointment);
        return getAppointmentByKode(appointment.getKode());
    }

    @Override
    public List<AppointmentModel> getListAppointment() {
        return appointmentDb.findAll();
    }

    @Override
    public AppointmentModel updateStatusAppointment(AppointmentModel appointment) {
        appointment.setIsDone(true);
        return appointmentDb.save(appointment);
    }

    public AppointmentModel createAppointment(AppointmentModel appointment){
        return appointmentDb.save(appointment);
    }

    @Override
    public List<AppointmentModel> getListAppointmentByDokterUsername(String username) {
        Optional<List<AppointmentModel>> appointment = appointmentDb.findAllByDokter_Username(username);
        return appointment.orElse(null);
    }

    @Override
    public List<AppointmentModel> getListAppointmentByPasienUsername(String username) {
        Optional<List<AppointmentModel>> appointment = appointmentDb.findAllByPasien_Username(username);
        return appointment.orElse(null);
    }
}
