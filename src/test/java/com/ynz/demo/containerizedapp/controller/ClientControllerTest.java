package com.ynz.demo.containerizedapp.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ynz.demo.containerizedapp.dto.ClientDto;
import com.ynz.demo.containerizedapp.dto.projection.ClientInfo;
import com.ynz.demo.containerizedapp.service.ClientService;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.web.context.WebApplicationContext;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
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

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void makeSureMockMvcIsReady() {
        assertNotNull(mvc);
    }

    @Test
    void isObjectMapperInjected() {
        assertNotNull(objectMapper);
    }

    @Test
    void whenGivenClientEmail_ShouldReturnClientInfo() throws Exception {
        String targetEmail = "ynz@hotmail.com";

        //arrange
        when(clientService.findClientByEmail(targetEmail)).thenReturn(new ClientInfo() {
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
        mvc.perform(MockMvcRequestBuilders.get("/client/" + targetEmail))
                .andExpect(status().isFound())
                .andExpect(MockMvcResultMatchers.jsonPath("$.name", is("Mike")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.email").value(targetEmail));
    }

    @Test
    void whenFindClientByInValidEmail_ReturnApiErrorResponse() throws Exception {
        mvc.perform(MockMvcRequestBuilders.get("/client/ynz"))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$.status").value("BAD_REQUEST"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").isNotEmpty())
                .andExpect(MockMvcResultMatchers.jsonPath("$.errors", hasSize(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.timeStamp").exists());
    }

    @Test
    void testCreateClient() throws Exception {
        //arrange
        ClientDto clientDto = new ClientDto();
        clientDto.setEmail("ynz@gmail.com");
        clientDto.setName("mike");

        when(clientService.createNewClient(any(ClientDto.class))).thenReturn(clientDto);

        //act and assert
        mvc.perform(MockMvcRequestBuilders.post("/client")
                .contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(clientDto))
        )
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value("mike"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.email").value("ynz@gmail.com"))
                .andExpect(MockMvcResultMatchers.header().exists("Location"))
                .andExpect(MockMvcResultMatchers.header().string("Location", Matchers.containsString("ynz@gmail.com")));

        verify(clientService).createNewClient(any(ClientDto.class));
    }

    @Test
    void missingNameAsCreatingClient_ThenTriggeringConstrainViolation() throws Exception {
        //arrange
        ClientDto clientDto = new ClientDto();
        clientDto.setEmail("ynz@gmail.com");

        //act and assert
        mvc.perform(MockMvcRequestBuilders.post("/client")
                .contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(clientDto))
        ).andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @Test
    void missingEmailAsCreatingClient_ThenTriggeringConstrainViolation() throws Exception {
        //arrange
        ClientDto clientDto = new ClientDto();
        clientDto.setName("mike");

        //act and assert
        mvc.perform(MockMvcRequestBuilders.post("/client")
                .contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(clientDto))
        )
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$.status").value(HttpStatus.BAD_REQUEST.name()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").exists())
                .andExpect(MockMvcResultMatchers.jsonPath("$.errors").exists())
                .andExpect(MockMvcResultMatchers.jsonPath("$.timeStamp").exists());
    }

    @Test
    void missingClientRequestBody_ThenTriggerMessageNotReadableException() throws Exception {

        //act and assert
        mvc.perform(MockMvcRequestBuilders.post("/client")
                .contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(null))
        )
                .andExpect(status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$.status").value(Matchers.is(HttpStatus.BAD_REQUEST.name())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value(is("Http message is not readable")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.errors[0]").value(Matchers.containsString("Required request body is missing")));
    }

}