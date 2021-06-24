package com.ynz.demo.containerizedapp.repository;

import com.ynz.demo.containerizedapp.domain.Client;
import com.ynz.demo.containerizedapp.domain.Order;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.jdbc.Sql;

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Sql("classpath:client_order_test_data.sql")
class ClientRepositoryTest {
    @Autowired
    private ClientRepository clientRepository;

    @Test
    void findClientOrdersByEmail() {

        List<Order> orderList = clientRepository.findClientOrdersByEmail("ynz@hotmail.com");
        assertThat(orderList, hasSize(2));
    }

    @Test
    void findClientByEmail(){

        Client client = clientRepository.findByEmail("ynz@hotmail.com");
        assertThat(client.getName(),is("ynz"));
    }

}