package com.tugaskelompok.rumahsehat.obat.projections;

import java.time.LocalDateTime;

public interface MonthlyObatIncomeProjections {
    LocalDateTime getTanggal();
    Integer getPemasukan();
}
