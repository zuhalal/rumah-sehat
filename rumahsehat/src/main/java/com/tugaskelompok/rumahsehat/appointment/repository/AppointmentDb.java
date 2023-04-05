package com.tugaskelompok.rumahsehat.appointment.repository;
import com.tugaskelompok.rumahsehat.appointment.model.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AppointmentDb extends JpaRepository<AppointmentModel, String> {
    // JPA
    Optional<AppointmentModel> findByKode(String kode);
    Optional<List<AppointmentModel>> findAllByDokter_Username(String username);
    Optional<List<AppointmentModel>> findAllByPasien_Username(String username);
}
