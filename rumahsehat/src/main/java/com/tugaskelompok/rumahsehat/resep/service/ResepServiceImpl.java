package com.tugaskelompok.rumahsehat.resep.service;

import com.tugaskelompok.rumahsehat.apoteker.model.ApotekerModel;
import com.tugaskelompok.rumahsehat.jumlah.model.JumlahModel;
import com.tugaskelompok.rumahsehat.resep.model.*;
import com.tugaskelompok.rumahsehat.resep.repository.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.*;

@Service
@Transactional
public class ResepServiceImpl implements ResepService {
    @Autowired
    ResepDb resepDb;

    @Override
    public void addResep(ResepModel resep) {
        resepDb.save(resep);
    }

    @Override
    public ResepModel getById(String id) {
        Optional<ResepModel> resep = resepDb.findById(Long.parseLong(id));

        if (resep.isPresent()) {
            return resep.get();
        }

        return null;
    }

    @Override
    public boolean checkStokObat(List<JumlahModel> listJumlah) {
        for (JumlahModel jumlahObat: listJumlah) {
            if (jumlahObat.getObat().getStok() < jumlahObat.getKuantitas()) {
                return false;
            }
        }

        return true;
    }

    @Override
    public ResepModel confirmResep(ResepModel resep, ApotekerModel apoteker) {
        resep.setConfirmerUUID(apoteker);
        resep.setIsDone(true);
        resepDb.save(resep);

        return getById(resep.getId().toString());
    }

    @Override
    public List<ResepModel> getListResep() {
        return resepDb.findAll();
    }

    @Override
    public ResepModel getByKodeAppointment(String kode) {
        Optional<ResepModel> resep = resepDb.findByKodeAppointment_Kode(kode);

        if (resep.isPresent()) {
            return resep.get();
        }

        return null;
    }
}