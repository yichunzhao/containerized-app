package com.ynz.demo.containerizedapp.controller;

import com.ynz.demo.containerizedapp.dto.projection.ClientInfo;
import com.ynz.demo.containerizedapp.service.ClientService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.web.context.WebApplicationContext;

import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ClientController.class)
class ClientControllerTest {

    @MockBean
    private ClientService clientService;

    @Autowired
    private WebApplicationContext context;

    @Autowired
    private MockMvc mvc;

    @Test
    void makeSureMockMvcIsReady() {
        assertNotNull(mvc);
    }

    @Test
    void whenGivenClientEmail_ShouldReturnClient() throws Exception {

        //arrange
        when(clientService.findClientByEmail("ynz@hotmail.com")).thenReturn(new ClientInfo() {
            @Override
            public String getName() {
                return "Mike";
            }

            @Override
            public String getEmail() {
                return "ynz@hotmail.com";
            }
        });

        //act and assert
        mvc.perform(MockMvcRequestBuilders.get("/client/ynz@hotmail.com"))
                .andExpect(status().isFound())
                .andExpect(MockMvcResultMatchers.jsonPath("$.name", is("Mike")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.email").value("ynz@hotmail.com"))
        ;
    }

}