package com.tugaskelompok.rumahsehat.pasien.service;

import com.tugaskelompok.rumahsehat.pasien.model.*;
import java.util.List;

public interface PasienService {
    PasienModel addPasien(String nama, String username, String password, int umur, String email);
    List<PasienModel> getListPasien();
    PasienModel getPasienByUsername(String username);
    String encrypt(String password);
    void pembayaran(String username, Integer jumlahTagihan);
}