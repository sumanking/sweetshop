package com.incubyte.sweetshop.controller;

import com.incubyte.sweetshop.model.Sweet;
import com.incubyte.sweetshop.service.SweetService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(SweetController.class)
class SweetControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private SweetService sweetService;

    // 🔴 RED — controller should return all sweets
    @Test
    void shouldReturnAllSweets() throws Exception {

        Sweet sweet = new Sweet();
        sweet.setId(1L);
        sweet.setName("Rasgulla");
        sweet.setPrice(20);
        sweet.setQuantity(10);

        when(sweetService.getAllSweets()).thenReturn(List.of(sweet));

        mockMvc.perform(get("/sweets"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("Rasgulla"));
    }
}
