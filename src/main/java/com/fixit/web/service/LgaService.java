package com.fixit.web.service;

import com.fixit.web.entity.Lga;
import com.fixit.web.repository.LgaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class LgaService {

    @Autowired
    private LgaRepository lgaRepository;

    @Value("${spring.data.web.pageable.default-page-size}")
    private int pageSize;

    public Page<Lga> listAll(final int pageNumber) {
        final Pageable pageable = PageRequest.of(pageNumber - 1, pageSize);
        return lgaRepository.findAll(pageable);
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
