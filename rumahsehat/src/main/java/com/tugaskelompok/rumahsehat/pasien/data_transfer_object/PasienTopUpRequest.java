package com.tugaskelompok.rumahsehat.pasien.data_transfer_object;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.Min;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class PasienTopUpRequest {
    @Min(value = 1L, message = "The nominal value must be positive")
    private Integer nominal;
}
