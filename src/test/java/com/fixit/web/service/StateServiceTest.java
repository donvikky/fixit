package com.fixit.web.service;

import com.fixit.web.entity.State;
import com.fixit.web.repository.StateRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class StateServiceTest {

    @Mock
    private StateRepository stateRepository;
    private StateService stateService;

    private AutoCloseable autoCloseable;
    private State state;

    private List<State> states;

    @BeforeEach
    void setUp() {
        autoCloseable = MockitoAnnotations.openMocks(this);
        stateService = new StateService(stateRepository);
        state = new State("Lagos");
        states = Arrays.asList(
                new State("Delta"),
                new State("Plateau"),
                new State("FCT")
        );
    }

    @AfterEach
    void tearDown() throws Exception{
        stateRepository.deleteAll();
        autoCloseable.close();
    }

    @Test
    void itShouldListAllStates() {
        stateService.listAll();
        verify(stateRepository).findAll();
    }

    @Test
    @Disabled
    void ItShouldListAllStatesPaginated() {

    }

    @Test
    void save() {
        stateService.save(state);
        verify(stateRepository).save(state);
    }

    @Test
    void get() {
        int id = 1;
        state.setId(id);
        when(stateRepository.findById(id)).thenReturn(Optional.of(state));

        stateService.get(id);
        verify(stateRepository).findById(id);
    }

    @Test
    void delete() {
        int id = 1;
        stateService.delete(id);
        verify(stateRepository).deleteById(id);
    }

    @Test
    void findLastRecord() {
        stateService.findLastRecord();
        verify(stateRepository).findTopByOrderByIdDesc();
    }
}