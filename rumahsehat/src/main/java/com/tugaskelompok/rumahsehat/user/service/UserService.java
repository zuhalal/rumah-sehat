package com.tugaskelompok.rumahsehat.user.service;

import com.tugaskelompok.rumahsehat.user.model.UserModel;

public interface UserService {
    UserModel addUser(UserModel user);
    public String encrypt(String password);
    UserModel getUserByUsername(String username);
    UserModel getUserByEmail(String email);
    UserModel deleteUser(UserModel user);
    UserModel updateUser(UserModel user);
    String checkPassword(String password);
}
