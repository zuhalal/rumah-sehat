package com.tugaskelompok.rumahsehat.apoteker.repository;

import com.tugaskelompok.rumahsehat.apoteker.model.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ApotekerDb extends JpaRepository<ApotekerModel, String> {
    Optional<ApotekerModel> findById(String id);
    Optional<ApotekerModel> findByUsername(String username);
}