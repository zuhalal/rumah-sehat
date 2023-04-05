package com.tugaskelompok.rumahsehat.pasien.repository;

import com.tugaskelompok.rumahsehat.pasien.model.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PasienDb extends JpaRepository<PasienModel, String> {
    Optional<PasienModel> findById(String id);
    Optional<PasienModel>  findByUsername(String username);
}