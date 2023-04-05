package com.tugaskelompok.rumahsehat.obat.service;

import com.tugaskelompok.rumahsehat.jumlah.model.JumlahModel;
import com.tugaskelompok.rumahsehat.obat.data_transfer_object.DailyObatDTO;
import com.tugaskelompok.rumahsehat.obat.data_transfer_object.PeriodicObatDTO;
import com.tugaskelompok.rumahsehat.obat.data_transfer_object.PeriodicObatResponseDTO;
import com.tugaskelompok.rumahsehat.obat.projections.DailyObatIncomeProjections;
import com.tugaskelompok.rumahsehat.obat.projections.MonthlyObatIncomeProjections;
import com.tugaskelompok.rumahsehat.obat.projections.ObatKuantitasProjections;
import com.tugaskelompok.rumahsehat.obat.projections.ObatTotalPendapatanProjections;
import com.tugaskelompok.rumahsehat.obat.model.ObatModel;
import com.tugaskelompok.rumahsehat.obat.repository.ObatDb;
import com.tugaskelompok.rumahsehat.utils.MonthInAYear;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

@Service
@Transactional
public class ObatServiceImpl implements ObatService{

    @Autowired
    ObatDb obatDb;

    @Override
    public List<ObatModel> getListObat() {
        return obatDb.findAllByOrderByNamaObatAsc();
    }

    @Override
    public ObatModel getObatById(String id) {
        Optional<ObatModel> obat = obatDb.findById(id);
        if (obat.isPresent()) {
            return obat.get();
        } else {
            throw new NoSuchElementException();
        }
    }

    public ObatModel updateStokObat(ObatModel obat, int newStok) {
        obat.setStok(newStok);
        return obatDb.save(obat);
    }

    @Override
    public Integer getHargaFromListJumlahObat(List<JumlahModel> listJumlahObat) {
        Integer harga = 0;

        for (JumlahModel jumlahObat: listJumlahObat) {
            Integer total =  jumlahObat.getObat().getHarga() * jumlahObat.getKuantitas();
            harga += total;
        }

        return harga;
    }

    @Override
    public ObatModel calculateStock(ObatModel obat, Integer kuantitas) {
        obat.setStok(obat.getStok() - kuantitas);
        obatDb.save(obat);
        return obat;
    }

    @Override
    public List<MonthlyObatIncomeProjections> getAllMonthlyIncomeThisYear() {
        Optional<List<MonthlyObatIncomeProjections>> monthlyObatIncome = obatDb.findMonthlyObatIncomeThisYear();
        if (monthlyObatIncome.isPresent()) {
            return monthlyObatIncome.get();
        }
        return null;
    }

    @Override
    public List<Integer> findMissingMonth(List<MonthlyObatIncomeProjections> monthlyObatIncome) {
        List<Integer> res = new ArrayList<>();

        for (var i = 1; i <= 12; i++) {
            int j;
            var isFound = false;

            for (j = 0; j < monthlyObatIncome.size(); j++) {
                Integer monthVal = monthlyObatIncome.get(j).getTanggal().getMonthValue();

                if (i == monthVal) {
                    isFound = true;
                    break;
                }
            }

            if (!isFound) {
                res.add(i);
            }
        }

        return res;
    }

    @Override
    public List<ObatKuantitasProjections> getAllObatKuantitasProjections(List<String> daftarObatRaw) {
        List<String> daftarObat = daftarObatRaw.stream().distinct().collect(Collectors.toList());
        List<ObatKuantitasProjections> data = new ArrayList<>();
        for(var i=0; i < daftarObat.size(); i++){
            if(!daftarObat.get(i).equals("None")){
                Optional<ObatKuantitasProjections> obatKuantitasProjections = obatDb.findKuantitasObat(daftarObat.get(i));
                if (obatKuantitasProjections.isPresent()) {
                    data.add(obatKuantitasProjections.get());
                }    
            }
        }
        return data;
    }

    @Override
    public List<ObatTotalPendapatanProjections> getAllObatTotalPendapatanProjections(List<String> daftarObatRaw) {
        List<String> daftarObat = daftarObatRaw.stream().distinct().collect(Collectors.toList());
        List<ObatTotalPendapatanProjections> data = new ArrayList<>();
        for(var i=0; i < daftarObat.size(); i++){
            if(!daftarObat.get(i).equals("None")){
                Optional<ObatTotalPendapatanProjections> obatTotalPendapatanProjections = obatDb.findPendapatanObat(daftarObat.get(i));
                if (obatTotalPendapatanProjections.isPresent()) {
                    data.add(obatTotalPendapatanProjections.get());
                }    
            }
        }
        return data;
    }

    @Override
    public List<MonthlyObatIncomeProjections> getObatMonhtlyIncome(String namaObat){
        Optional<List<MonthlyObatIncomeProjections>> monthlyObatIncome = obatDb.findMonthlyIncomeOfSpecificObat(namaObat);
        if (monthlyObatIncome.isPresent()) {
            return monthlyObatIncome.get();
        }
        return null;
    }

    @Override
    public List<DailyObatIncomeProjections> getObatDailyIncome(String idObat){
        Optional<List<DailyObatIncomeProjections>> dailyObatIncome = obatDb.findDailyIncomeOfObat(idObat);
        if (dailyObatIncome.isPresent()) {
            return dailyObatIncome.get();
        }
        return null;
    }

    @Override
    public List<Integer> findMissingDates(List<DailyObatIncomeProjections> dailyObatIncome){
        List<Integer> res = new ArrayList<>();

        for (var i = 1; i <= 31; i++) {
            int j;
            var isFound = false;

            for (j = 0; j < dailyObatIncome.size(); j++) {
                Integer dayVal =dailyObatIncome.get(j).getTanggal().getDayOfMonth();

                if (i == dayVal) {
                    isFound = true;
                    break;
                }
            }

            if (!isFound) {
                res.add(i);
            }
        }

        return res;
    }

    @Override
    public List<PeriodicObatResponseDTO> fillMissingMonthsAndSortMonths(List<MonthlyObatIncomeProjections> monthlyObatIncomeResponses) {
        List<PeriodicObatDTO> tempData = new ArrayList<>();

        // fill current months existing in database
        for (MonthlyObatIncomeProjections monthlyObatIncome: monthlyObatIncomeResponses) {
            tempData.add(new PeriodicObatDTO(monthlyObatIncome.getTanggal().getMonthValue(), monthlyObatIncome.getPemasukan()));
        }

        List<Integer> missingMonths = findMissingMonth(monthlyObatIncomeResponses);

        // fill missing months
        if (!missingMonths.isEmpty()) {
            for (Integer missingMonth: missingMonths) {
                tempData.add(new PeriodicObatDTO(missingMonth, 0));
            }
        }

        List<PeriodicObatResponseDTO> finalData = new ArrayList<>();

        // sort months
        if (!tempData.isEmpty()) {
            tempData.sort(Comparator.comparingInt(PeriodicObatDTO::getBulanKe));
            for (PeriodicObatDTO monthlyObatIncome: tempData) {
                var bulanString = MonthInAYear.arrMonth.get(monthlyObatIncome.getBulanKe()-1);
                finalData.add(new PeriodicObatResponseDTO(bulanString, monthlyObatIncome.getPemasukan()));
            }
        }

        return finalData;
    }

    @Override
    public List<PeriodicObatResponseDTO> lineCharts(String idObat) {
        List<MonthlyObatIncomeProjections> monthlyObatIncomeResponses = getObatMonhtlyIncome(idObat);
        List<Integer> missingMonths = findMissingMonth(monthlyObatIncomeResponses);

        List<PeriodicObatDTO> tempData = new ArrayList<>();
        List<PeriodicObatResponseDTO> finalData = new ArrayList<>();

        for (MonthlyObatIncomeProjections monthlyObatIncome: monthlyObatIncomeResponses) {
            tempData.add(new PeriodicObatDTO(monthlyObatIncome.getTanggal().getMonthValue(), monthlyObatIncome.getPemasukan()));
        }

        if (!missingMonths.isEmpty()) {
            for (Integer missingMonth: missingMonths) {
                tempData.add(new PeriodicObatDTO(missingMonth, 0));
            }
        }

        if (!tempData.isEmpty()) {
            tempData.sort(Comparator.comparingInt(PeriodicObatDTO::getBulanKe));
            for (PeriodicObatDTO monthlyObatIncome: tempData) {
                var bulanString = MonthInAYear.arrMonth.get(monthlyObatIncome.getBulanKe()-1);
                finalData.add(new PeriodicObatResponseDTO(bulanString, monthlyObatIncome.getPemasukan()));
            }
        }
        return finalData;
    }

    @Override
    public List<DailyObatDTO> lineChartsDaily(String idObat) {
        List<DailyObatIncomeProjections> dailyObatIncomeResponses = getObatDailyIncome(idObat);

        List<Integer> missingDates = findMissingDates(dailyObatIncomeResponses);

        List<DailyObatDTO> tempData = new ArrayList<>();

        for (DailyObatIncomeProjections dailyObatIncome: dailyObatIncomeResponses) {
            tempData.add(new DailyObatDTO(dailyObatIncome.getTanggal().getDayOfMonth(), dailyObatIncome.getPemasukan()));
        }

        if (!missingDates.isEmpty()) {
            for (Integer missingDate: missingDates) {
                tempData.add(new DailyObatDTO(missingDate, 0));
            }
        }

        return tempData;
    }
}

