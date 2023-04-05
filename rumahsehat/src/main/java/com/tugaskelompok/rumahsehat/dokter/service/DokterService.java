package com.tugaskelompok.rumahsehat.dokter.service;

import com.tugaskelompok.rumahsehat.dokter.data_transfer_object.*;
import com.tugaskelompok.rumahsehat.dokter.model.*;
import java.util.List;

public interface DokterService {
    void addDokter(DokterRequestDTO dokterDTO);
    List<DokterModel> getListDokter();
    String encrypt(String password);
    DokterModel getDokterbyId(String id);
}