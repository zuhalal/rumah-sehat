package com.tugaskelompok.rumahsehat.pasien.service;

import com.tugaskelompok.rumahsehat.pasien.model.PasienModel;
import com.tugaskelompok.rumahsehat.pasien.repository.PasienDb;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
@Transactional
public class PasienRestServiceImpl implements PasienRestService {
    @Autowired
    PasienDb pasienDb;

    @Override
    public PasienModel getPasienByUsername(String username) {
        Optional<PasienModel> pasien = pasienDb.findByUsername(username);
        if (pasien.isPresent()) {
            return pasien.get();
        }

        throw new NoSuchElementException();
    }

    @Override
    public PasienModel topUpSaldo(String username, Integer saldo) {
        PasienModel pasien = getPasienByUsername(username);

        pasien.setSaldo(pasien.getSaldo() + saldo);
        pasienDb.save(pasien);
        return pasien;
    }
}
