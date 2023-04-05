package com.tugaskelompok.rumahsehat.service;

import com.tugaskelompok.rumahsehat.admin.model.AdminModel;
import com.tugaskelompok.rumahsehat.admin.repository.AdminDb;
import com.tugaskelompok.rumahsehat.admin.service.AdminServiceImpl;
import com.tugaskelompok.rumahsehat.user.model.UserRole;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class AdminServiceTest {
    @Mock
    private AdminDb adminDb;

    @InjectMocks
    AdminServiceImpl adminService;

    AdminModel getAdmin() {
        AdminModel adminModel1 = new AdminModel();
        adminModel1.setNama("Zuhal");
        adminModel1.setRole(UserRole.ADMIN);
        adminModel1.setIsSso(true);
        adminModel1.setEmail("zuhal@gmail.com");
        adminModel1.setUsername("zuhal.alimul");
        adminModel1.setPassword("rumahsehat");
        return adminModel1;
    }

    @BeforeEach
    void setUp() {
        AdminModel adminModel1 = new AdminModel();
        adminModel1.setNama("Zuhal");
        adminModel1.setRole(UserRole.ADMIN);
        adminModel1.setIsSso(true);
        adminModel1.setEmail("zuhal@gmail.com");
        adminModel1.setUsername("zuhal.alimul");
        adminModel1.setPassword("rumahsehat");

        Mockito.when(adminDb.save(Mockito.any(AdminModel.class))).thenReturn(adminModel1);
    }

    @Test
    void saveAdminTest() {
        var created = adminService.addAdmin(getAdmin());

        Assertions.assertEquals(getAdmin().getUuid(), created.getUuid());
        Assertions.assertEquals(getAdmin().getNama(), created.getNama());
        Assertions.assertEquals(getAdmin().getEmail(), created.getEmail());
        Assertions.assertEquals(getAdmin().getIsSso(), created.getIsSso());
        Assertions.assertEquals(getAdmin().getRole(), created.getRole());
    }

    @Test
    void whiteListAdminCheck() {
        var created = adminService.addAdmin(getAdmin());
        boolean isAdmin = adminService.whiteListAdminCheck(created.getUsername());

        Assertions.assertTrue(isAdmin);
    }
}
