package com.tugaskelompok.rumahsehat.obat.projections;

import java.time.LocalDateTime;

public interface DailyObatIncomeProjections {
    LocalDateTime getTanggal();
    Integer getPemasukan();
}
