package com.tugaskelompok.rumahsehat.obat.projections;
import com.tugaskelompok.rumahsehat.obat.model.ObatModel;

import java.io.Serializable;

public class ObatTotalPendapatanProjections implements Serializable{
    ObatModel obat;
    Long pendapatan;

    public ObatTotalPendapatanProjections(ObatModel obat, Long pendapatan) {
        this.obat = obat;
        this.pendapatan = pendapatan;
    }

    public ObatModel getObat(){
        return this.obat;
    }

    public Long getKuantitas(){
        return this.pendapatan;
    }
}
