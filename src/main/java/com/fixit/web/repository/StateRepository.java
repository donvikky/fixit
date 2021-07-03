package com.fixit.web.repository;

import com.fixit.web.entity.State;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StateRepository extends JpaRepository<State, Integer>{
    Page findAll(Pageable pageable);
    State findTopByOrderByIdDesc();
}
