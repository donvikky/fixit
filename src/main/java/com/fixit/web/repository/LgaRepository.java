package com.fixit.web.repository;

import com.fixit.web.entity.Lga;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LgaRepository extends JpaRepository<Lga, Integer> {
    Page findAll(Pageable pageable);
}
