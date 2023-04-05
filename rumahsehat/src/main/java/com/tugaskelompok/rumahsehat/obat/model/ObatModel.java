package com.tugaskelompok.rumahsehat.obat.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.tugaskelompok.rumahsehat.jumlah.model.JumlahModel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.List;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "obat")
public class ObatModel implements Serializable {
    @Id
    @GeneratedValue(generator = "system-uuid")
    @GenericGenerator(name = "system-uuid", strategy = "uuid")
    @Column(name = "id_obat", unique = true)
    private String idObat;

    @NotNull
    @Column(name = "nama_obat", nullable = false)
    private String namaObat;

    @NotNull
    @Column(name = "harga", nullable = false)
    private Integer harga;

    @NotNull
    @Column(name = "stok", nullable = false, columnDefinition = "integer default 100")
    private Integer stok = 100;

    @OneToMany(mappedBy = "obat", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JsonIgnore
    private List<JumlahModel> listJumlah;
}
