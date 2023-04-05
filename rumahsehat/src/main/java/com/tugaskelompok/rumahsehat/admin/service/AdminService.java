package com.tugaskelompok.rumahsehat.admin.service;


import com.tugaskelompok.rumahsehat.admin.model.AdminModel;

public interface AdminService {
    AdminModel addAdmin(AdminModel admin);
    String encrypt(String password);
    boolean whiteListAdminCheck(String username);
}
