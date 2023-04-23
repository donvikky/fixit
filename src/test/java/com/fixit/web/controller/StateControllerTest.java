package com.fixit.web.controller;

import com.fixit.web.entity.State;
import com.fixit.web.service.StateService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.servlet.ModelAndView;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

@SpringBootTest
@AutoConfigureMockMvc
class StateControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private StateService stateService;

    @Test
    @WithMockUser(username = "test@test.com", password = "1234", roles = {"ADMIN"})
    void getStates() throws Exception{
        int currentPage = 1;
        MvcResult mvcResult = mockMvc
                                    .perform(MockMvcRequestBuilders.get("/states/page/{id}",  currentPage))
                                    .andExpect(MockMvcResultMatchers.status().isOk())
                                    .andExpect(MockMvcResultMatchers.view().name("states/list"))
                                    .andReturn();

        ModelAndView mav = mvcResult.getModelAndView();
        assertTrue(mav.getModel().containsKey("states"));
        assertTrue(mav.getModel().containsKey("currentPage"));
    }

    @Test
    @WithMockUser(username = "test@test.com", password = "1234", roles = {"ADMIN"})
    void itShouldShowAddStateView() throws Exception{
        mockMvc
                .perform(MockMvcRequestBuilders.get("/states/create"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.view().name("states/create"))
                .andExpect(MockMvcResultMatchers.model().attributeExists("state"))
                .andReturn();
    }

    @Test
    @WithMockUser(username = "test@test.com", password = "1234", roles = {"ADMIN"})
    void itShouldCreateState() throws Exception{
        int currentPage = 1;
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("name", "Lagos");

        // Perform POST request to "/create" with the State object and assert the response
        mockMvc.perform(MockMvcRequestBuilders
                        .post("/states/create")
                        .params(params)
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED))

                .andExpect(MockMvcResultMatchers.status().is3xxRedirection())
                .andExpect(MockMvcResultMatchers.redirectedUrl("/states/page/" + currentPage))
                .andExpect(MockMvcResultMatchers.flash().attributeExists("message"))
                .andExpect(MockMvcResultMatchers.flash().attribute("message", "The state has been added successfully"));
    }

    @Test
    @WithMockUser(username = "test@test.com", password = "1234", roles = {"ADMIN"})
    void itShouldShowStateEditForm() throws Exception{
        int stateId = 1;
        State state = new State("Lagos");
        state.setId(stateId);

        when(stateService.get(stateId)).thenReturn(state);
        mockMvc
                .perform(MockMvcRequestBuilders.get("/states/edit/{id}", state.getId()))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.view().name("states/edit"))
                .andExpect(MockMvcResultMatchers.model().attributeExists("state"))
                .andExpect(MockMvcResultMatchers.model().attributeExists("states"))
                .andReturn();
    }

    @Test
    @WithMockUser(username = "test@test.com", password = "1234", roles = {"ADMIN"})
    void itShouldUpdateState() throws Exception{
        int currentPage = 1;
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("name", "Lagos");

        mockMvc
                .perform(MockMvcRequestBuilders
                        .post("/states/edit")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .params(params))
                .andExpect(MockMvcResultMatchers.status().is3xxRedirection())
                .andExpect(MockMvcResultMatchers.redirectedUrl("/states/page/" + currentPage))
                .andExpect(MockMvcResultMatchers.flash().attributeExists("message"))
                .andExpect(MockMvcResultMatchers.flash().attribute("message", "The state has been updated successfully"))
                .andReturn();
    }

    @Test
    @WithMockUser(username = "test@test.com", password = "1234", roles = {"ADMIN"})
    void delete() throws Exception{
        int currentPage = 1;
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("id", Integer.toString(currentPage));

        mockMvc
                .perform(MockMvcRequestBuilders
                        .post("/states/delete")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .params(params))
                .andExpect(MockMvcResultMatchers.status().is3xxRedirection())
                .andExpect(MockMvcResultMatchers.redirectedUrl("/states/page/" + currentPage))
                .andReturn();
    }
}