package com.tugaskelompok.rumahsehat.pasien.service;

import com.tugaskelompok.rumahsehat.pasien.model.*;
import com.tugaskelompok.rumahsehat.pasien.repository.*;
import com.tugaskelompok.rumahsehat.user.model.UserRole;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import javax.transaction.Transactional;
import java.util.*;

@Service
@Transactional
public class PasienServiceImpl implements PasienService {
    @Autowired
    PasienDb pasienDb;

    @Override
    public List<PasienModel> getListPasien() {
        return pasienDb.findAll();
    }

    @Override
    public PasienModel getPasienByUsername(String username) {
        Optional<PasienModel> pasien = pasienDb.findByUsername(username);
        if (pasien.isPresent()) {
            return pasien.get();
        }

        return null;
    }

    @Override
    public PasienModel addPasien(String nama, String username, String password, int umur, String email) {
        var pasien = new PasienModel();
        String pass = encrypt(password);
        pasien.setEmail(email);
        pasien.setNama(nama);
        pasien.setUmur(umur);
        pasien.setUsername(username);
        pasien.setSaldo(0);
        pasien.setPassword(pass);
        pasien.setRole(UserRole.PASIEN);
        pasien.setIsSso(false);
        
        return pasienDb.save(pasien);
    }

    @Override
    public String encrypt(String password) {
        var passwordEncoder = new BCryptPasswordEncoder();
        return passwordEncoder.encode(password);
    }

    @Override
    public void pembayaran(String username, Integer jumlahTagihan) {
        PasienModel pasien = getPasienByUsername(username);

        pasien.setSaldo(pasien.getSaldo() - jumlahTagihan);
        pasienDb.save(pasien);
    }
}

