package com.tugaskelompok.rumahsehat.obat.data_transfer_object;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ObatDTO {
    private String idObat;
    private String namaObat;
    private Integer stok = 100;
}


