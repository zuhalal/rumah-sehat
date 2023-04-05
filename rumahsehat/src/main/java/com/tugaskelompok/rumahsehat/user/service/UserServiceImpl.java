package com.tugaskelompok.rumahsehat.user.service;

import com.tugaskelompok.rumahsehat.user.model.UserModel;
import com.tugaskelompok.rumahsehat.user.repository.UserDb;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.regex.*;
import javax.transaction.Transactional;

@Service
@Transactional
public class UserServiceImpl implements UserService {
    @Autowired
    UserDb userDb;

    @Override
    public UserModel addUser(UserModel user) {
        String pass = encrypt(user.getPassword());
        user.setPassword(pass);
        return userDb.save(user);
    }

    @Override
    public String encrypt(String password) {
        var passwordEncoder = new BCryptPasswordEncoder();
        return passwordEncoder.encode(password);
    }

    @Override
    public UserModel getUserByUsername(String username) {
        return userDb.findByUsername(username);
    }

    @Override
    public UserModel getUserByEmail(String email) {
        return userDb.findByEmail(email);
    }

    @Override
    public UserModel updateUser(UserModel user) {
        return userDb.save(user);
    }

    @Override
    public UserModel deleteUser(UserModel user) {
        userDb.delete(user);
        return user;
    }

    @Override
    public String checkPassword(String password){
        String regex = "^(?=.*[a-z])(?=."
                       + "*[A-Z])(?=.*\\d)"
                       + "(?=.*[-+_!@#$%^&*., ?]).+$";
        if(password.length() < 8){
            return "Password harus terdiri dari minimal 8 karakter";
        }

        var p = Pattern.compile(regex);
        var m = p.matcher(password);

        if(!m.matches()) return "Password harus mengandung angka, huruf besar, huruf kecil, dan simbol";
        return "correct";
    }
}
