package com.tugaskelompok.rumahsehat.pasien.service;

import com.tugaskelompok.rumahsehat.pasien.model.PasienModel;

public interface PasienRestService {
    PasienModel getPasienByUsername(String username);
    PasienModel topUpSaldo(String username, Integer saldo);
}
