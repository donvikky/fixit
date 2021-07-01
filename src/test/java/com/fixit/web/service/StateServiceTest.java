package com.fixit.web.service;

import com.fixit.web.entity.State;
import com.fixit.web.repository.StateRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SpringBootTest
class StateServiceTest {

    @Mock
    StateRepository stateRepository;

    ArgumentCaptor<Pageable> pageableCaptor = ArgumentCaptor.forClass(Pageable.class);

    @InjectMocks
    StateService stateService;

    State state;

    final int pageNumber = 2;
    final int pageSize = 5;

    @BeforeEach
    void setUp() {
        state = new State();
    }

    @Test
    void listAll() {
        List<State> states = new ArrayList<>();
        states.add(state);

        when(stateRepository.findAll()).thenReturn(states);
        List<State> foundStates = stateRepository.findAll();

        verify(stateRepository).findAll();
        assertThat(foundStates).hasSize(1);
    }

    @Test
    void listAllPaginated() {
        /*
        List<State> states = new ArrayList<>();
        states.add(state);

        when(stateRepository.findAll(pageableCaptor.capture())).thenReturn((Page) State.class);
        Page<State> foundStates = stateService.listAllPaginated(anyInt());

        verify(stateRepository).findAll(pageableCaptor.capture());
        PageRequest pageable = (PageRequest) pageableCaptor.getValue();
        assertThat(pageable).isNull();
        *
         */
        List<State> stats = new ArrayList<>();
        stats.add(state);

        when(stateRepository.findAll(pageableCaptor.capture())).thenReturn(any(Page.class));

        Pageable pageable = PageRequest.of(2,2);
        Page<State> states = stateRepository.findAll(pageableCaptor.capture());
        assertThat(states.getTotalElements()).isEqualTo(1);
    }

    @Test
    void save() {
        when(stateRepository.save(any(State.class))).thenReturn(state);

        State savedState = stateService.save(state);
        verify(stateRepository).save(any(State.class));
        assertThat(savedState).isNotNull();
    }

    @Test
    void get() {
        when(stateRepository.findById(anyInt())).thenReturn(Optional.of(state));
        State savedState = stateService.get(1);
        verify(stateRepository).findById(anyInt());
        assertThat(savedState).isNotNull();
    }

    @Test
    void delete() {
        stateService.delete(1);
        verify(stateRepository).deleteById(anyInt());
    }
}