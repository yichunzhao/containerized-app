package com.ynz.demo.containerizedapp.controller;

import com.ynz.demo.containerizedapp.dto.ClientDto;
import com.ynz.demo.containerizedapp.exceptions.ApiError;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.util.UriComponentsBuilder;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.net.URI;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Acceptance testing is known as a user acceptance testing, end user testing.
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT, properties = {"server.port=8081"})
@Testcontainers(disabledWithoutDocker = true)
@Slf4j
public class ClientControllerAcceptanceTest {
    private static final String CLIENTS_URL = "client";

    @Container
    protected static PostgreSQLContainer<?> postgreSQLContainer = new PostgreSQLContainer<>("postgres:13")
            .withDatabaseName("postgres").withPassword("test").withUsername("postgres");

    //>=SpringBoot 2.2.6, it allows dynamically modify the application property values
    @DynamicPropertySource
    protected static void setPostgreSQLProperties(DynamicPropertyRegistry dynamicPropertyRegistry) {
        dynamicPropertyRegistry.add("spring.datasource.url", postgreSQLContainer::getJdbcUrl);
    }

    @Value("${server.port}")
    private int testServerPort;

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    void isPortReady() {
        log.info("PORT: {}", testServerPort);
    }

    @Test
    void isTestReady() {
        assertAll(
                () -> assertTrue(postgreSQLContainer.isRunning()),
                () -> assertThat(testServerPort, is(8081)),
                () -> assertNotNull(restTemplate)
        );
    }

    @Test
    void findClientInFreshDbByEmail_ThenReturnStatusNotFound() {
        URI uri = UriComponentsBuilder.newInstance().scheme("http").host("localhost").port(testServerPort).path(CLIENTS_URL)
                .pathSegment("{email}").buildAndExpand("ynz@hotmail.com").toUri();

        ResponseEntity<ApiError> response = restTemplate.getForEntity(uri, ApiError.class);
        assertThat(response.getStatusCode(), is(HttpStatus.NOT_FOUND));
    }

    @Test
    @Sql("classpath:client_order_test_data.sql")
    @Transactional
    @Disabled("cannot populate db!")
    void findExistingClientInPopulatedDb_ThenReturnStatusFound() {
        log.info("Populating db first, and then find a client by its email");
        URI uri = UriComponentsBuilder.newInstance().scheme("http").host("localhost").port(testServerPort).path(CLIENTS_URL)
                .pathSegment("{email}").buildAndExpand("ynz@hotmail.com").toUri();

        log.info("sending a http request to server");
        ResponseEntity<ClientDto> response = restTemplate.getForEntity(uri, ClientDto.class);
        assertThat(response.getStatusCode(), is(HttpStatus.FOUND));
    }

    @Test
    @Transactional
    void createClientFormClientSide_AndThenFindItFromClientSide() {
        log.info("create a new client from client side via restTemplate: ");
        URI uri = UriComponentsBuilder.newInstance().scheme("http").host("localhost").port(testServerPort).path(CLIENTS_URL).build().toUri();
        log.info("request uri: {}", uri);
        ClientDto clientDto = new ClientDto();
        clientDto.setName("John");
        clientDto.setEmail("john@hotmail.com");

        ResponseEntity<ClientDto> response = restTemplate.postForEntity(uri, clientDto, ClientDto.class);
        assertThat(response.getStatusCode(), is(HttpStatus.CREATED));

        URI uriQ = UriComponentsBuilder.newInstance().scheme("http").host("localhost").port(testServerPort).path(CLIENTS_URL)
                .pathSegment("{email}").buildAndExpand("john@hotmail.com").toUri();

        ResponseEntity<ClientDto> responseQ = restTemplate.getForEntity(uriQ, ClientDto.class);
        assertThat(responseQ.getStatusCode(), is(HttpStatus.FOUND));
    }

}
