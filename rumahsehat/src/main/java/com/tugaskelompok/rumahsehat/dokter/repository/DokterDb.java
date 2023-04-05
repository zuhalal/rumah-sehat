package com.tugaskelompok.rumahsehat.dokter.repository;

import com.tugaskelompok.rumahsehat.dokter.model.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface DokterDb extends JpaRepository<DokterModel, String> {
    Optional<DokterModel> findById(String id);
}