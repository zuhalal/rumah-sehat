package com.tugaskelompok.rumahsehat.auth.data_transfer_object;

import com.tugaskelompok.rumahsehat.user.model.UserRole;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class RegisResponseDTO {
    private String uuid;
    private String username;
    private String nama;
    private String email;
    private int umur;
    private int saldo;
    private boolean isSso;
    private UserRole role;
}
