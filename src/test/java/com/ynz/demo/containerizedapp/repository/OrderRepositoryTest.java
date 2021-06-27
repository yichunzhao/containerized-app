package com.ynz.demo.containerizedapp.repository;

import com.ynz.demo.containerizedapp.domain.Order;
import com.ynz.demo.containerizedapp.dto.OrderInfo;
import com.ynz.demo.containerizedapp.shared.OrderStatus;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.jdbc.Sql;

import java.time.OffsetDateTime;
import java.util.UUID;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertAll;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Sql("classpath:client_order_test_data.sql")
class OrderRepositoryTest {
    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private TestEntityManager testEntityManager;

    @Test
    void findByBusinessId() {
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

}