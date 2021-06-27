package com.ynz.demo.containerizedapp.repository;

import com.ynz.demo.containerizedapp.domain.Client;
import com.ynz.demo.containerizedapp.domain.Order;
import com.ynz.demo.containerizedapp.domain.OrderItem;
import com.ynz.demo.containerizedapp.dto.OrderInfo;
import com.ynz.demo.containerizedapp.shared.OrderStatus;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.jdbc.Sql;

import java.time.OffsetDateTime;
import java.util.Optional;
import java.util.UUID;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Sql("classpath:client_order_test_data.sql")
@Slf4j
class OrderRepositoryTest {
    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private TestEntityManager testEntityManager;

    @Test
    void findByBusinessId() {
        log.info("findByBusinessId");
        Order order1 = testEntityManager.find(Order.class, 2);
        UUID order1BusinessId = order1.getBusinessId();

        OffsetDateTime expectedTimeStamp = OffsetDateTime.parse("1999-01-08T14:05:06+01");
        OrderInfo foundOrderInfo = orderRepository.findByBusinessId(order1BusinessId);

        assertAll(
                () -> assertThat(foundOrderInfo.getOrderStatus(), is(OrderStatus.SUSPENDING)),
                () -> assertThat(foundOrderInfo.getBusinessId(), is(order1BusinessId)),
                () -> assertThat(foundOrderInfo.getCreatedAt(), is(expectedTimeStamp))
        );
    }

    @Test
    void whenFindByOrderId_ThenClientEntityEagerlyLoaded() {
        log.info("whenFindByOrderId_ThenEagerLoadingClient");
        Optional<Order> retrieved = orderRepository.findById(2);
        assertTrue(retrieved.isPresent());

        Client client = retrieved.get().getClient();
        assertNotNull(client);
    }

    @Test
    void whenSavingOrderHavingOrderItem_ThenOrderItemCascadeOnPersist() {
        OrderItem item = new OrderItem();
        item.setProductName("iphone11");
        item.setAmount(1);

        Order order = new Order();
        order.setOrderStatus(OrderStatus.SUSPENDING);
        order.add(item);

        Order persisted = orderRepository.save(order);
        Integer id = persisted.getId();
        assertNotNull(id);

        Order found = testEntityManager.find(Order.class, id);
        assertThat(found.getOrderItems(), hasSize(1));
    }

}