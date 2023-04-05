package com.tugaskelompok.rumahsehat.resep.service;

import com.tugaskelompok.rumahsehat.resep.model.ResepModel;
import com.tugaskelompok.rumahsehat.resep.repository.ResepDb;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
@Transactional
public class ResepRestServiceImpl implements ResepRestService {
    @Autowired
    ResepDb resepDb;

    @Override
    public ResepModel getById(String id) {
        Optional<ResepModel> resep = resepDb.findById(Long.parseLong(id));

        if (resep.isPresent()) {
            return resep.get();
        }

        throw new NoSuchElementException();
    }
}
