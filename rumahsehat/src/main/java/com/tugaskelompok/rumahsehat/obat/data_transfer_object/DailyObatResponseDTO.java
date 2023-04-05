package com.tugaskelompok.rumahsehat.obat.data_transfer_object;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
public class DailyObatResponseDTO {
    private Integer tanggal;
    private Integer pemasukan;
}
