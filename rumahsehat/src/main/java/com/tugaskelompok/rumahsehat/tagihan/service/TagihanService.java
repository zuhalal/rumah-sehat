package com.tugaskelompok.rumahsehat.tagihan.service;

import com.tugaskelompok.rumahsehat.appointment.model.AppointmentModel;
import com.tugaskelompok.rumahsehat.tagihan.model.TagihanModel;

import java.util.List;

public interface TagihanService {
    TagihanModel addTagihan(TagihanModel tagihan);
    TagihanModel addTagihan(TagihanModel tagihan, AppointmentModel appointment);
    TagihanModel getByKode(String kode);
    TagihanModel pembayaran(String kode, String username);
    List<TagihanModel> getListTagihan();
    List<TagihanModel> getListTagihanByPasienUsername(String username);
}
