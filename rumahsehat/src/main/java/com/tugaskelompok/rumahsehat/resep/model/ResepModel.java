package com.tugaskelompok.rumahsehat.resep.model;

import com.tugaskelompok.rumahsehat.apoteker.model.ApotekerModel;
import com.tugaskelompok.rumahsehat.appointment.model.AppointmentModel;
import com.tugaskelompok.rumahsehat.jumlah.model.JumlahModel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "resep")
public class ResepModel implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(nullable = false)
    private Boolean isDone;

    @NotNull
    @Column(nullable = false)
    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm")
    private LocalDateTime createdAt;

    @ManyToOne(fetch = FetchType.EAGER, optional = true)
    @JoinColumn(name="confirmer_uuid", referencedColumnName = "user_uuid", nullable = true)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private ApotekerModel confirmerUUID;

    @OneToOne(cascade = CascadeType.ALL, optional = false)
    @JoinColumn(name="kode_appointment", referencedColumnName = "kode", nullable = false)
    private AppointmentModel kodeAppointment;

    @OneToMany(mappedBy = "resep", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private List<JumlahModel> listJumlah;
}
