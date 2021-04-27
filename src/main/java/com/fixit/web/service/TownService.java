package com.fixit.web.service;

import com.fixit.web.entity.Town;
import com.fixit.web.repository.TownRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TownService {

    @Autowired
    private TownRepository townRepository;

    public List<Town> listAll() {
        return townRepository.findAll();
    }

    public void save(Town town) {
        townRepository.save(town);
    }

    public Town get(int id) {
        return (Town) townRepository.findById(id).get();
    }

    public void delete(int id) {
        townRepository.deleteById(id);
    }
}
