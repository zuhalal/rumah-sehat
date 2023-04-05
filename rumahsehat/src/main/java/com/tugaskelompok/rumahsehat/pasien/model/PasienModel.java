package com.tugaskelompok.rumahsehat.pasien.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.tugaskelompok.rumahsehat.appointment.model.AppointmentModel;
import com.tugaskelompok.rumahsehat.user.model.UserModel;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.List;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "pasien")
@PrimaryKeyJoinColumn(name = "user_uuid")
public class PasienModel extends UserModel {
    @NotNull
    @Column(name = "saldo", nullable = false)
    private Integer saldo;

    @NotNull
    @Column(name = "umur", nullable = false)
    private Integer umur;

    @OneToMany(mappedBy = "pasien", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JsonIgnore
    private List<AppointmentModel> listAppointment;
}