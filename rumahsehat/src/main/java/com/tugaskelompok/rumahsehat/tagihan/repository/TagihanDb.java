package com.tugaskelompok.rumahsehat.tagihan.repository;

import com.tugaskelompok.rumahsehat.tagihan.model.TagihanModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TagihanDb extends JpaRepository<TagihanModel, String> {
    Optional<TagihanModel> findByKode(String kode);
    Optional<List<TagihanModel>> findAllByKodeAppointment_Pasien_Username(String username);
}
