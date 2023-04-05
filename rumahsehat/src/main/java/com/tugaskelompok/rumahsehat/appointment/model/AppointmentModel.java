package com.tugaskelompok.rumahsehat.appointment.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.tugaskelompok.rumahsehat.config.StringPrefixedSequenceIdGenerator;
import com.tugaskelompok.rumahsehat.dokter.model.DokterModel;
import com.tugaskelompok.rumahsehat.pasien.model.PasienModel;
import com.tugaskelompok.rumahsehat.resep.model.ResepModel;
import com.tugaskelompok.rumahsehat.tagihan.model.TagihanModel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.LocalDateTime;

import org.hibernate.annotations.Parameter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "appointment")
public class AppointmentModel implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "APT")
    @GenericGenerator(
            name = "APT",
            strategy = "com.tugaskelompok.rumahsehat.config.StringPrefixedSequenceIdGenerator",
            parameters = {
                    @Parameter(name = StringPrefixedSequenceIdGenerator.INCREMENT_PARAM, value = "1"),
                    @Parameter(name = StringPrefixedSequenceIdGenerator.VALUE_PREFIX_PARAMETER, value = "APT-")})
    private String kode;

    @NotNull
    @Column(nullable = false)
    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm")
    private LocalDateTime waktuAwal;

    @NotNull
    @Column(nullable = false)
    private Boolean isDone = false;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name="pasien", referencedColumnName = "user_uuid", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private PasienModel pasien;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name="dokter", referencedColumnName = "user_uuid", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private DokterModel dokter;

    @OneToOne(mappedBy = "kodeAppointment")
    @JsonIgnore
    private TagihanModel tagihan;

    @OneToOne(mappedBy = "kodeAppointment")
    @JsonIgnore
    private ResepModel resep;
}
