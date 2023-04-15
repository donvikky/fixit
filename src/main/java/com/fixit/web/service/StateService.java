package com.fixit.web.service;

import com.fixit.web.entity.State;
import com.fixit.web.repository.StateRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StateService {
    private StateRepository stateRepository;

    @Autowired
    public StateService(StateRepository stateRepository) {
        this.stateRepository = stateRepository;
    }

    @Value("${spring.data.web.pageable.default-page-size}")
    private int pageSize;

    public List<State> listAll(){
        return stateRepository.findAll();
    }

    public Page<State> listAllPaginated(final int pageNumber) {
        final Pageable pageable = PageRequest.of(pageNumber - 1, pageSize);
        return stateRepository.findAll(pageable);
    }

    public State save(State state) {
        return stateRepository.save(state);
    }

    public State get(int id) {
        return (State) stateRepository.findById(id).get();
    }

    public void delete(int id) {
        stateRepository.deleteById(id);
    }

    public State findLastRecord(){
        return stateRepository.findTopByOrderByIdDesc();
    }
}
