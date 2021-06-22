package com.fixit.web.repository;

import com.fixit.web.entity.Craft;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CraftRepository extends JpaRepository<Craft, Integer> {
    Page findAll(Pageable pageable);
}
