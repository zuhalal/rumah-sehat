package com.tugaskelompok.rumahsehat.apoteker.service;

import com.tugaskelompok.rumahsehat.apoteker.data_transfer_object.*;
import com.tugaskelompok.rumahsehat.apoteker.model.*;
import java.util.List;

public interface ApotekerService {
    void addApoteker(ApotekerRequestDTO apotekerDTO);
    String encrypt(String password);
    List<ApotekerModel> getListApoteker();
    ApotekerModel getApotekerByUsername(String username);
}