package com.tugaskelompok.rumahsehat.resep.data_transfer_object;

import com.tugaskelompok.rumahsehat.jumlah.model.JumlahModel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ResepDTO {
    private List<JumlahModel> listJumlah;
}


