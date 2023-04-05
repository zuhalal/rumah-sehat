package com.tugaskelompok.rumahsehat.resep.repository;

import com.tugaskelompok.rumahsehat.resep.model.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ResepDb extends JpaRepository<ResepModel, String> {
    Optional<ResepModel> findById(Long id);
    Optional<ResepModel> findByKodeAppointment_Kode(String kode);
}