package com.ynz.demo.containerizedapp.repository;

import com.ynz.demo.containerizedapp.domain.Client;
import com.ynz.demo.containerizedapp.domain.Order;
import com.ynz.demo.containerizedapp.exceptions.ClientNotFoundException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.jdbc.Sql;

import java.util.List;
import java.util.Set;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Sql("classpath:client_order_test_data.sql")
class ClientRepositoryTest {
    @Autowired
    private ClientRepository clientRepository;

    @Autowired
    private TestEntityManager testEntityManager;

    @Test
    void findClientOrdersByEmail() {
        List<Order> orderList = clientRepository.findClientOrdersByEmail("ynz@hotmail.com");
        assertThat(orderList, hasSize(2));
    }

    @Test
    void findClientByEmail() {
        assertTrue(clientRepository.findByEmail("ynz@hotmail.com").isPresent());
    }

    @Test
    void createNewClientWithoutOrder_IsAllowed() {
        Client newClient = new Client();
        newClient.setEmail("hotmail");
        newClient.setName("Mikkel");
        Client persisted = clientRepository.save(newClient);

        assertAll(
                () -> assertNotNull(persisted),
                () -> assertThat(persisted.getId(), is(notNullValue()))
        );
    }

    @Test
    void whenClientDelete_NoActionOnOrders() throws Exception {
        //Given
        int clientId = 1;
        int orderId1 = 2;
        int orderId2 = 3;

        //this client has two orders
        Client client = testEntityManager.find(Client.class, clientId);
        Set<Order> orders = client.getOrders();
        assertThat(orders, hasSize(2));

        //delete client
        clientRepository.deleteById(clientId);

        //Order references Client, on delete No Action
        Order order1_ = testEntityManager.find(Order.class, orderId1);
        Order order2_ = testEntityManager.find(Order.class, orderId2);

        assertNotNull(order1_);
        assertNotNull(order2_);
    }

}