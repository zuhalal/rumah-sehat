package com.tugaskelompok.rumahsehat.appointment.service;

import com.tugaskelompok.rumahsehat.appointment.model.AppointmentModel;

import java.util.List;

public interface AppointmentService {
    AppointmentModel updateAppointmentStatusToDone(AppointmentModel appointment);
    AppointmentModel getAppointmentByKode(String kode);
    List<AppointmentModel> getListAppointment();
    AppointmentModel updateStatusAppointment(AppointmentModel appointment);
    AppointmentModel createAppointment(AppointmentModel appointment);
    List<AppointmentModel> getListAppointmentByDokterUsername(String username);
    List<AppointmentModel> getListAppointmentByPasienUsername(String username);
}
