package com.fixit.web.service;

import com.fixit.web.entity.Lga;
import com.fixit.web.repository.LgaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LgaService {

    private LgaRepository lgaRepository;

    @Value("${spring.data.web.pageable.default-page-size}")
    private int pageSize;

    @Autowired
    public LgaService(LgaRepository lgaRepository) {
        this.lgaRepository = lgaRepository;
    }

    public Page<Lga> listAll(final int pageNumber) {
        final Pageable pageable = PageRequest.of(pageNumber - 1, pageSize);
        return lgaRepository.findAll(pageable);
    }

    public List<Lga> listAll(){
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
