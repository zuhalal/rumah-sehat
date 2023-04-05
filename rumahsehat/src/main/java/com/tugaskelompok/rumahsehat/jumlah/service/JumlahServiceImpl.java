package com.tugaskelompok.rumahsehat.jumlah.service;

import com.tugaskelompok.rumahsehat.jumlah.model.JumlahModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.tugaskelompok.rumahsehat.jumlah.repository.JumlahDb;

import javax.transaction.Transactional;

@Service
@Transactional
public class JumlahServiceImpl implements JumlahService{
    @Autowired
    JumlahDb jumlahDb;

    @Override
    public void addJumlah(JumlahModel jumlah){
        jumlahDb.save(jumlah);
    }

    @Override
    public void deleteJumlah(JumlahModel jumlah){
        jumlahDb.delete(jumlah);
    }
}
