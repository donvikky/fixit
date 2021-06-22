package com.fixit.web.service;

import com.fixit.web.entity.Craft;
import com.fixit.web.repository.CraftRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CraftService {

    private CraftRepository craftRepository;

    @Value("${spring.data.web.pageable.default-page-size}")
    private int pageSize;

    @Autowired
    public CraftService(CraftRepository craftRepository) {
        this.craftRepository = craftRepository;
    }

    public List<Craft> listAll() {
        return craftRepository.findAll();
    }

    public Page<Craft> listAll(final int pageNumber) {
        final Pageable pageable = PageRequest.of(pageNumber - 1, pageSize);
        return craftRepository.findAll(pageable);
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
