package com.ynz.demo.containerizedapp.repository;

import com.ynz.demo.containerizedapp.domain.OrderItem;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.jdbc.Sql;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Sql("classpath:client_order_test_data.sql")
@Slf4j
class OrderItemRepositoryTest {
    @Autowired
    private OrderItemRepository orderItemRepository;

    @Autowired
    private TestEntityManager testEntityManager;

    @Test
    void whenSavingOrderItemWithoutOrder_ThenTransientEntityGetPK(){
        OrderItem orderItem = new OrderItem();
        orderItem.setProductName("iphone12");
        orderItem.setAmount(1);

        OrderItem persisted = orderItemRepository.save(orderItem);
        assertNotNull(persisted);
        assertNotNull(persisted.getId());
        assertNull(persisted.getOrder());
    }

}