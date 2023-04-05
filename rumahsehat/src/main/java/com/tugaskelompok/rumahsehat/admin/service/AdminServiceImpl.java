package com.tugaskelompok.rumahsehat.admin.service;

import com.tugaskelompok.rumahsehat.admin.model.AdminModel;
import com.tugaskelompok.rumahsehat.admin.repository.AdminDb;
import com.tugaskelompok.rumahsehat.config.SsoWhitelist;
import com.tugaskelompok.rumahsehat.user.model.UserRole;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
@Transactional
public class AdminServiceImpl implements AdminService {
    @Autowired
    AdminDb adminDb;

    @Override
    public AdminModel addAdmin(AdminModel admin) {
        String pass = encrypt(admin.getPassword());
        admin.setPassword(pass);
        admin.setRole(UserRole.ADMIN);
        return adminDb.save(admin);
    }

    @Override
    public String encrypt(String password) {
        var passwordEncoder = new BCryptPasswordEncoder();
        return passwordEncoder.encode(password);
    }

    @Override
    public boolean whiteListAdminCheck(String username) {
        var isAdmin = false;

        for (String ssoUsername: SsoWhitelist.LIST_ADMIN) {
            if (ssoUsername.equals(username)) {
                isAdmin = true;
            }
        }

        return isAdmin;
    }
}
