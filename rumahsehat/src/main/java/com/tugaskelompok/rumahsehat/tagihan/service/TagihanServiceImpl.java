package com.tugaskelompok.rumahsehat.tagihan.service;

import com.tugaskelompok.rumahsehat.appointment.model.AppointmentModel;
import com.tugaskelompok.rumahsehat.tagihan.model.TagihanModel;
import com.tugaskelompok.rumahsehat.tagihan.repository.TagihanDb;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.tugaskelompok.rumahsehat.pasien.service.PasienService;
import java.time.format.DateTimeFormatter;

import javax.transaction.Transactional;
import java.time.LocalDateTime;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class TagihanServiceImpl implements TagihanService {
    @Autowired
    TagihanDb tagihanDb;

    @Autowired
    PasienService pasienService;

    @Override
    public TagihanModel addTagihan(TagihanModel tagihan) {
        return tagihanDb.save(tagihan);
    }

    @Override
    public TagihanModel addTagihan(TagihanModel tagihan, AppointmentModel appointment) {
        tagihan.setJumlahTagihan(appointment.getDokter().getTarif());
        tagihan.setTanggalTerbuat(LocalDateTime.now());
        tagihan.setKodeAppointment(appointment);
        tagihan.setIsPaid(false);
        return tagihanDb.save(tagihan);
    }

    @Override
    public TagihanModel getByKode(String kode) {
        Optional<TagihanModel> tagihan = tagihanDb.findByKode(kode);

        if (tagihan.isPresent()) {
            return tagihan.get();
        }

        return null;
    }

    @Override
    public TagihanModel pembayaran(String kode, String username){
        TagihanModel tagihan = getByKode(kode);
        pasienService.pembayaran(username, tagihan.getJumlahTagihan());
        tagihan.setIsPaid(true);
        var now = LocalDateTime.now();
        var formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm");
        
        tagihan.setTanggalBayar(LocalDateTime.parse(now.format(formatter)));
        tagihanDb.save(tagihan);
        return tagihan;
    }

    @Override
    public List<TagihanModel> getListTagihan() {
        return tagihanDb.findAll();
    }

    @Override
    public List<TagihanModel> getListTagihanByPasienUsername(String username) {
        Optional<List<TagihanModel>> tagihan = tagihanDb.findAllByKodeAppointment_Pasien_Username(username);
        return tagihan.orElse(null);
    }

}
