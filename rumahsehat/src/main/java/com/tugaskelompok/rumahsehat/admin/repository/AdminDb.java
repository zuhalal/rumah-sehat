package com.tugaskelompok.rumahsehat.admin.repository;

import com.tugaskelompok.rumahsehat.admin.model.AdminModel;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AdminDb extends JpaRepository<AdminModel, Long> {
}
