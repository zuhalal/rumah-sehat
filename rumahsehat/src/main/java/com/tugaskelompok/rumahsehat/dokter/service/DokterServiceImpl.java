package com.tugaskelompok.rumahsehat.dokter.service;

import com.tugaskelompok.rumahsehat.dokter.data_transfer_object.*;
import com.tugaskelompok.rumahsehat.dokter.model.*;
import com.tugaskelompok.rumahsehat.dokter.repository.*;
import com.tugaskelompok.rumahsehat.user.model.UserRole;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import javax.transaction.Transactional;
import java.util.*;

@Service
@Transactional
public class DokterServiceImpl implements DokterService {
    @Autowired
    DokterDb dokterDb;

    @Override
    public void addDokter(DokterRequestDTO dokterDTO) {
        var dokter = new DokterModel();
        String pass = encrypt(dokterDTO.getPassword());
        
        dokter.setEmail(dokterDTO.getEmail());
        dokter.setNama(dokterDTO.getNama());
        dokter.setTarif(dokterDTO.getTarif());
        dokter.setUsername(dokterDTO.getUsername());
        dokter.setPassword(pass);
        dokter.setRole(UserRole.DOKTER);
        dokter.setIsSso(false);
        dokterDb.save(dokter);
    }

    @Override
    public List<DokterModel> getListDokter() {
        return dokterDb.findAll();
    }
    @Override
    public String encrypt(String password) {
        var passwordEncoder = new BCryptPasswordEncoder();
        return passwordEncoder.encode(password);
    }
    @Override
    public DokterModel getDokterbyId(String id){
        Optional<DokterModel> dokter = dokterDb.findById(id);
        if(dokter.isPresent()) {
            return dokter.get();
        } else return null;
    }
}

