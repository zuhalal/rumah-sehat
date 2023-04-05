package com.tugaskelompok.rumahsehat.config.auth;

import com.tugaskelompok.rumahsehat.pasien.model.PasienModel;
import com.tugaskelompok.rumahsehat.pasien.repository.PasienDb;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Service
public class JwtUserDetailsService implements UserDetailsService {
    @Autowired
    PasienDb pasienDb;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<PasienModel> pasien = pasienDb.findByUsername(username);

        if (pasien.isPresent()) {
            Set<GrantedAuthority> grantedAuthorities = new HashSet<>();
            grantedAuthorities.add(new SimpleGrantedAuthority(pasien.get().getRole().toString()));
            return new User(pasien.get().getUsername(), pasien.get().getPassword(), grantedAuthorities);
        }

        return null;
    }
}
