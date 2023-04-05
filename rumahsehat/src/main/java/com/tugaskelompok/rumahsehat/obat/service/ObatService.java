package com.tugaskelompok.rumahsehat.obat.service;

import com.tugaskelompok.rumahsehat.jumlah.model.JumlahModel;
import com.tugaskelompok.rumahsehat.obat.data_transfer_object.DailyObatDTO;
import com.tugaskelompok.rumahsehat.obat.data_transfer_object.PeriodicObatResponseDTO;
import com.tugaskelompok.rumahsehat.obat.projections.DailyObatIncomeProjections;
import com.tugaskelompok.rumahsehat.obat.projections.MonthlyObatIncomeProjections;
import com.tugaskelompok.rumahsehat.obat.projections.ObatKuantitasProjections;
import com.tugaskelompok.rumahsehat.obat.model.ObatModel;
import com.tugaskelompok.rumahsehat.obat.projections.ObatTotalPendapatanProjections;

import java.util.List;

public interface ObatService {
    List<ObatModel> getListObat();
    ObatModel getObatById(String id);
    ObatModel updateStokObat(ObatModel obat, int newStok);
    Integer getHargaFromListJumlahObat(List<JumlahModel> listJumlahObat);
    ObatModel calculateStock(ObatModel obat, Integer kuantitas);
    List<MonthlyObatIncomeProjections> getAllMonthlyIncomeThisYear();
    List<Integer> findMissingMonth(List<MonthlyObatIncomeProjections> monthlyObatIncome);
    List<ObatKuantitasProjections> getAllObatKuantitasProjections(List<String> daftarObat);
    List<ObatTotalPendapatanProjections> getAllObatTotalPendapatanProjections(List<String> daftarObatRaw);
    List<MonthlyObatIncomeProjections> getObatMonhtlyIncome(String namaObat);
    List<DailyObatIncomeProjections> getObatDailyIncome(String idObat);
    List<Integer> findMissingDates(List<DailyObatIncomeProjections> dailyObatIncome);
    List<PeriodicObatResponseDTO> fillMissingMonthsAndSortMonths(List<MonthlyObatIncomeProjections> monthlyObatIncomeResponses);
    List<DailyObatDTO> lineChartsDaily(String idObat);
    List<PeriodicObatResponseDTO> lineCharts(String idObat);
}
