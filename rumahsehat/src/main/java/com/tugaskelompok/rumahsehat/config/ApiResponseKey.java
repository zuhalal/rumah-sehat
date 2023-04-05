package com.tugaskelompok.rumahsehat.config;

public class ApiResponseKey {
    private ApiResponseKey() {
        throw new IllegalStateException("Utility class");
    }
    public static final String STATUS = "status";
    public static final String STATUS_CODE = "status_code";
    public static final  String MESSAGE = "message";
    public static final String DATA = "data";
}
