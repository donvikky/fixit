package com.fixit.web.service;

import com.fixit.web.entity.Craft;
import com.fixit.web.repository.CraftRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CraftService {

    @Autowired
    private CraftRepository craftRepository;

    public List<Craft> listAll() {
        return craftRepository.findAll();
    }

    public void save(Craft craft) {
        craftRepository.save(craft);
    }

    public Craft get(int id) {
        return (Craft) craftRepository.findById(id).get();
    }

    public void delete(int id) {
        craftRepository.deleteById(id);
    }
}
