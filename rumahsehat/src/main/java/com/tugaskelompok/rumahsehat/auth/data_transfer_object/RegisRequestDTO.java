package com.tugaskelompok.rumahsehat.auth.data_transfer_object;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class RegisRequestDTO {
    private String username;
    private String nama;
    private String email;
    private int umur;
    private String password;
}
