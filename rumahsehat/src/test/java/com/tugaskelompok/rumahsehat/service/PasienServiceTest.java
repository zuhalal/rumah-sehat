package com.tugaskelompok.rumahsehat.service;

import com.tugaskelompok.rumahsehat.pasien.model.PasienModel;
import com.tugaskelompok.rumahsehat.pasien.repository.PasienDb;
import com.tugaskelompok.rumahsehat.pasien.service.PasienRestServiceImpl;
import com.tugaskelompok.rumahsehat.pasien.service.PasienServiceImpl;
import com.tugaskelompok.rumahsehat.user.model.UserRole;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.junit.jupiter.api.Assertions;

import java.util.Optional;

@ExtendWith(MockitoExtension.class)
class PasienServiceTest {
    @Spy
    @InjectMocks
    PasienServiceImpl pasienService;

    @Spy
    @InjectMocks
    PasienRestServiceImpl pasienRestService;

    @Mock
    PasienDb pasienDb;

    PasienModel pasien;

    @BeforeEach
    void setUp() {
        pasien = new PasienModel();
        pasien.setNama("Zuhal");
        pasien.setRole(UserRole.PASIEN);
        pasien.setIsSso(false);
        pasien.setEmail("pasiensatu@gmail.com");
        pasien.setUsername("pasiensatu");
        pasien.setPassword("Acd123!");
        pasien.setSaldo(0);

        Mockito.when(pasienDb.save(Mockito.any(PasienModel.class))).thenReturn(pasien);

        pasienDb.save(pasien);
    }

    @Test()
    void getPasienByUsernameTest() {
        Mockito.lenient().when(pasienDb.findByUsername("pasiensatu")).thenReturn(Optional.of(pasien));

        PasienModel pasienModel1 = pasienService.getPasienByUsername("pasiensatu");

        Assertions.assertEquals(pasien.getUsername(), pasienModel1.getUsername());
        Assertions.assertEquals(pasien.getUuid(), pasienModel1.getUuid());
        Assertions.assertEquals(pasien.getNama(), pasienModel1.getNama());
        Assertions.assertEquals(pasien.getEmail(), pasienModel1.getEmail());
        Assertions.assertEquals(pasien.getIsSso(), pasienModel1.getIsSso());
        Assertions.assertEquals(pasien.getRole(), pasienModel1.getRole());
        Assertions.assertEquals(pasien.getSaldo(), pasienModel1.getSaldo());
    }

    @Test()
    void getPasienByUsernameRestTest() {
        Mockito.lenient().when(pasienDb.findByUsername("pasiensatu")).thenReturn(Optional.of(pasien));

        PasienModel pasienModel1 = pasienRestService.getPasienByUsername("pasiensatu");

        Assertions.assertEquals(pasien.getUsername(), pasienModel1.getUsername());
        Assertions.assertEquals(pasien.getUuid(), pasienModel1.getUuid());
        Assertions.assertEquals(pasien.getNama(), pasienModel1.getNama());
        Assertions.assertEquals(pasien.getEmail(), pasienModel1.getEmail());
        Assertions.assertEquals(pasien.getIsSso(), pasienModel1.getIsSso());
        Assertions.assertEquals(pasien.getRole(), pasienModel1.getRole());
        Assertions.assertEquals(pasien.getSaldo(), pasienModel1.getSaldo());
    }

    @Test()
    void topUpSaldoUsernameTest() {
        Mockito.lenient().when(pasienDb.findByUsername("pasiensatu")).thenReturn(Optional.of(pasien));

        PasienModel pasienModel = pasienRestService.topUpSaldo("pasiensatu", 12000);

        Assertions.assertEquals(12000, pasienModel.getSaldo());
    }
}
