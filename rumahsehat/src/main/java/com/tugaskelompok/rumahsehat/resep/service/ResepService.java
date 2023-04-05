package com.tugaskelompok.rumahsehat.resep.service;

import com.tugaskelompok.rumahsehat.apoteker.model.ApotekerModel;
import com.tugaskelompok.rumahsehat.jumlah.model.JumlahModel;
import com.tugaskelompok.rumahsehat.resep.model.*;

import java.util.List;

public interface ResepService {
    void addResep(ResepModel resep);
    ResepModel getById(String id);
    boolean checkStokObat(List<JumlahModel> listJumlah);
    ResepModel confirmResep(ResepModel resep, ApotekerModel apoteker);
    List<ResepModel> getListResep();
    ResepModel getByKodeAppointment(String kode);
}
