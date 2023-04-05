package com.tugaskelompok.rumahsehat.utils;

import lombok.Getter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Getter
public class MonthInAYear {
    private MonthInAYear() {
        throw new IllegalStateException("Utility class");
    }
    public static final  List<String> arrMonth = new ArrayList<>(Arrays.asList("Januari", "Februari", "Maret", "April", "Mei", "Juni", "Juli", "Agustus", "September", "Oktober", "November", "Desember"));
}
