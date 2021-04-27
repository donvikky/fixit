package com.fixit.web.service;

import com.fixit.web.entity.State;
import com.fixit.web.repository.StateRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StateService {

    @Autowired
    private StateRepository stateRepository;

    public List<State> listAll() {
        return stateRepository.findAll();
    }

    public void save(State state) {
        stateRepository.save(state);
    }

    public State get(int id) {
        return (State) stateRepository.findById(id).get();
    }

    public void delete(int id) {
        stateRepository.deleteById(id);
    }
}
