package com.ynz.demo.containerizedapp.repository;


import com.ynz.demo.containerizedapp.AbstractUsingTestContainers;
import com.ynz.demo.containerizedapp.domain.Client;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.jdbc.Sql;
import org.testcontainers.junit.jupiter.Testcontainers;

import javax.sql.DataSource;
import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Testcontainers
@Sql("classpath:client_order_test_data.sql")
@Slf4j
public class TestClientRepoUsingTestContainers extends AbstractUsingTestContainers {

    @Autowired
    private DataSource dataSource;

    @Autowired
    private TestEntityManager testEntityManager;

    @Autowired
    private ClientRepository clientRepository;

    @Test
    void isPostgreSQLContainerReady() {
        assertTrue(postgreSQLContainer.isRunning());
    }

    @Test
    void contextLoads() {
        assertNotNull(dataSource);
        assertNotNull(testEntityManager);
    }

    @Test
    void showTstDbContainerJDBCURL() {
        log.info("dynamic generated jdbcUrl: {}", postgreSQLContainer.getJdbcUrl());
    }

    @Test
    void saveClientToTestContainerDb() {
        Client client = new Client();
        client.setName("Mike");
        client.setEmail("mike@gmail.com");
        assertThat(client.getId(), is(0));

        Client persisted = clientRepository.save(client);

        assertAll(
                () -> assertNotNull(persisted),
                () -> assertThat(client.getId(), is(not(0))),
                () -> assertThat(client.getName(), is("Mike"))
        );
    }

    @Test
    void findClientFromTestContainerDb() {

        //act
        Optional<Client> found = clientRepository.findByEmail("ynz@hotmail.com", Client.class);

        //assure
        assertAll(
                () -> assertTrue(found.isPresent()),
                () -> assertThat(found.get().getName(), is("ynz"))
        );
    }

    @Test
    void testPopulatingData() {
        Optional<Client> found = clientRepository.findById(1);
        assertTrue(found.isPresent());
    }

}
