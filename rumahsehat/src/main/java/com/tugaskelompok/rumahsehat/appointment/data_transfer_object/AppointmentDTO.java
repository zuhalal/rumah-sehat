package com.tugaskelompok.rumahsehat.appointment.data_transfer_object;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class AppointmentDTO {
    private LocalDateTime waktuAwal;
    private String dokterId;
}

