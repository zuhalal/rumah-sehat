package com.tugaskelompok.rumahsehat.obat.data_transfer_object;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
public class PeriodicObatResponseDTO {
    private String bulan;
    private Integer pemasukan;
}
