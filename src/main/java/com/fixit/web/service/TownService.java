package com.fixit.web.service;

import com.fixit.web.entity.Town;
import com.fixit.web.repository.TownRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class TownService {

    private TownRepository townRepository;

    @Value("${spring.data.web.pageable.default-page-size}")
    private int pageSize;

    @Autowired
    public TownService(TownRepository townRepository){
        this.townRepository = townRepository;
    }

    public Page<Town> listAll(final int pageNumber) {
        final Pageable pageable = PageRequest.of(pageNumber - 1, pageSize);
        return townRepository.findAll(pageable);
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
