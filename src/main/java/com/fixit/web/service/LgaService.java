package com.fixit.web.service;

import com.fixit.web.entity.Lga;
import com.fixit.web.repository.LgaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LgaService {

    @Autowired
    private LgaRepository lgaRepository;

    public List<Lga> listAll() {
        return lgaRepository.findAll();
    }

    public void save(Lga lga) {
        lgaRepository.save(lga);
    }

    public Lga get(int id) {
        return (Lga) lgaRepository.findById(id).get();
    }

    public void delete(int id) {
        lgaRepository.deleteById(id);
    }
}
