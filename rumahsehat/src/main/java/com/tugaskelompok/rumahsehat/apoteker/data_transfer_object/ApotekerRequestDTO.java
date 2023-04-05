package com.tugaskelompok.rumahsehat.apoteker.data_transfer_object;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class ApotekerRequestDTO {
    private String username;
    private String nama;
    private String email;
    private String password;
}
