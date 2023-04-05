package com.tugaskelompok.rumahsehat.apoteker.service;

import com.tugaskelompok.rumahsehat.apoteker.data_transfer_object.*;

import com.tugaskelompok.rumahsehat.apoteker.model.*;
import com.tugaskelompok.rumahsehat.apoteker.repository.*;
import com.tugaskelompok.rumahsehat.user.model.UserRole;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import javax.transaction.Transactional;
import java.util.*;

@Service
@Transactional
public class ApotekerServiceImpl implements ApotekerService {
    @Autowired
    ApotekerDb apotekerDb;

    @Override
    public void addApoteker(ApotekerRequestDTO apotekerDTO) {
        var apoteker = new ApotekerModel();
        String pass = encrypt(apotekerDTO.getPassword());

        apoteker.setEmail(apotekerDTO.getEmail());
        apoteker.setNama(apotekerDTO.getNama());
        apoteker.setUsername(apotekerDTO.getUsername());
        apoteker.setPassword(pass);
        apoteker.setRole(UserRole.APOTEKER);
        apoteker.setIsSso(false);
        apotekerDb.save(apoteker);
    }

    @Override
    public List<ApotekerModel> getListApoteker() {
        return apotekerDb.findAll();
    }

    @Override
    public String encrypt(String password) {
        var passwordEncoder = new BCryptPasswordEncoder();
        return passwordEncoder.encode(password);
    }

    @Override
    public ApotekerModel getApotekerByUsername(String username) {
        Optional<ApotekerModel> apoteker = apotekerDb.findByUsername(username);

        if (apoteker.isPresent()) {
            return apoteker.get();
        }

        return null;
    }
}

