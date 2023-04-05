package com.tugaskelompok.rumahsehat.config;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SsoWhitelist {
    private SsoWhitelist() {
        throw new IllegalStateException("Utility class");
    }
    public static final List<String> LIST_ADMIN = new ArrayList<>(Arrays.asList("zuhal.alimul", "amira.husna01", "andrea.debora", "neiva.annur", "andi.afifah"));
}
