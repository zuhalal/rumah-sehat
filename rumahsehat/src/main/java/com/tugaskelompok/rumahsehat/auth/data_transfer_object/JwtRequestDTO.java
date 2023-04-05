package com.tugaskelompok.rumahsehat.auth.data_transfer_object;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class JwtRequestDTO {
    private String username;
    private String password;
}
