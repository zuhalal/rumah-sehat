package com.tugaskelompok.rumahsehat.dokter.data_transfer_object;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class DokterRequestDTO {
    private String username;
    private String nama;
    private String email;
    private int tarif;
    private String password;
}
