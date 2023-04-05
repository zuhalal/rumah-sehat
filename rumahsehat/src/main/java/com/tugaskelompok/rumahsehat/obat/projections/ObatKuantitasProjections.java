package com.tugaskelompok.rumahsehat.obat.projections;
import com.tugaskelompok.rumahsehat.obat.model.ObatModel;

import java.io.Serializable;

public class ObatKuantitasProjections implements Serializable{
    ObatModel obat;
    Long kuantitas;

    public ObatKuantitasProjections(ObatModel obat, Long kuantitas) {
        this.obat = obat;
        this.kuantitas = kuantitas;
    }

    public ObatModel getObat(){
        return this.obat;
    }

    public Long getKuantitas(){
        return this.kuantitas;
    }
}
