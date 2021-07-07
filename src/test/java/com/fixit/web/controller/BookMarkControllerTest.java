package com.fixit.web.controller;

import com.fixit.web.service.BookMarkService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@WithUserDetails
@ActiveProfiles("test")
class BookMarkControllerTest {

    @Autowired
    private BookMarkService bookMarkService;

    @Autowired
    private MockMvc mockMvc;

    final int FIRST_PAGE = 1;

    @Test
    void getStates() throws Exception{
        mockMvc.perform(get("/bookmarks/page/" + FIRST_PAGE))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("bookmarks"))
                .andExpect(model().attributeExists("currentPage"))
                .andExpect(view().name("bookmarks/list"));
    }

    @Test
    void saveBookMark() {
    }

    @Test
    void deleteBookMark() {
    }
}