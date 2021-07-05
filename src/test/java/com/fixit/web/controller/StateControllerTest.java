package com.fixit.web.controller;

import com.fixit.web.entity.State;
import com.fixit.web.service.StateService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.util.NoSuchElementException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@WithUserDetails
@ActiveProfiles("test")
class StateControllerTest {

    @Autowired
    StateService stateService;

    @Autowired
    MockMvc mockMvc;

    private State state;

    final int FIRST_PAGE = 1;

    @Test
    void getStates() throws Exception {
        mockMvc.perform(get("/states/page/" + FIRST_PAGE))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("states"))
                .andExpect(model().attributeExists("currentPage"))
                .andExpect(view().name("states/list"));
    }

    @Test
    void addState() throws Exception {
        mockMvc.perform(get("/states/create"))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("state"))
                .andExpect(view().name("states/create"));
    }

    @Test
    void createState() throws Exception {
        mockMvc.perform(post("/states/create")
                .param("name", "state 1"))
                .andDo(print())
                .andExpect(status().is3xxRedirection())
                .andExpect(flash().attributeExists("message"))
                .andExpect(view().name("redirect:/states/page/1"));

        State state1 = stateService.findLastRecord();
        assertThat(state1.getName()).isEqualTo("state 1");
    }

    @Test
    void createInvalidState() throws Exception{
        mockMvc.perform(post("/states/create")
                .param("name", "st"))
                .andExpect(status().isOk())
                .andExpect(model().attributeHasFieldErrors("state", "name"))
                .andExpect(view().name("states/create"));
    }

    @Test
    void edit() throws Exception{
        State state = stateService.save(new State("state 1"));
        mockMvc.perform(get("/states/edit/"+ state.getId()))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("state"))
                .andExpect(model().attributeExists("states"))
                .andExpect(view().name("states/edit"));
    }

    @Test
    void editWhenIdNotFoundThrowsNoSuchElementException() throws Exception{
        mockMvc.perform(get("/states/edit/1000000"))
                .andExpect(status().isOk())
                .andExpect(result -> {assertThat(result.getResolvedException() instanceof NoSuchElementException);})
                .andExpect(result ->
                {assertThat(result.getResolvedException().getMessage()).isEqualTo("No value present");});
    }

    @Test
    void update() throws Exception {
        mockMvc.perform(post("/states/edit")
                .param("id", "1")
                .param("name", "state 1"))
                .andExpect(status().is3xxRedirection())
                .andExpect(flash().attributeExists("message"))
                .andExpect(view().name("redirect:/states/page/1"));
    }

    @Test
    void updateInvalidState() throws Exception {
        mockMvc.perform(post("/states/edit")
                .param("id", "1")
                .param("name", "st"))
                .andExpect(status().isOk())
                .andExpect(model().attributeHasFieldErrors("state", "name"))
                .andExpect(view().name("states/edit"));
    }

    @Test
    void delete() throws Exception {
        state = new State("state 1");
        stateService.save(state);
        State savedState = stateService.findLastRecord();

        mockMvc.perform(post("/states/delete")
                .param("id", String.valueOf(savedState.getId())))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/states/page/1"));
    }
}