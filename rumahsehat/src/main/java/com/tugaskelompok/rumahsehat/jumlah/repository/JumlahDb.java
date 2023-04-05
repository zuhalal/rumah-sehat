package com.tugaskelompok.rumahsehat.jumlah.repository;
import com.tugaskelompok.rumahsehat.jumlah.model.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface JumlahDb extends JpaRepository<JumlahModel,String>{
}
