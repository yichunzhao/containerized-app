package com.ynz.demo.containerizedapp.repository;

import com.ynz.demo.containerizedapp.domain.Client;
import com.ynz.demo.containerizedapp.domain.Order;
import com.ynz.demo.containerizedapp.domain.OrderItem;
import com.ynz.demo.containerizedapp.dto.projection.OrderInfo;
import com.ynz.demo.containerizedapp.shared.OrderStatus;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.jdbc.Sql;

import java.time.OffsetDateTime;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
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
        Optional<OrderInfo> foundOrderInfoOpt = orderRepository.findByBusinessIdDynamic(order1BusinessId, OrderInfo.class);
        assertThat(foundOrderInfoOpt.isPresent(), is(true));

        OrderInfo foundOrderInfo = foundOrderInfoOpt.get();

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
        log.info("saving only non-null attributes");
        OrderItem item = new OrderItem();
        item.setProductName("iphone11");
        item.setAmount(1);

        //order businessIs is null at this moment
        Order order = new Order();
        order.setCreatedAt(OffsetDateTime.now());
        order.setOrderStatus(OrderStatus.SUSPENDING);
        order.add(item);

        Order persisted = orderRepository.save(order);
        Integer id = persisted.getId();
        assertNotNull(id);

        Order found = testEntityManager.find(Order.class, id);
        assertThat(found.getOrderItems(), hasSize(1));
    }

    @Test
    @DisplayName("within a transaction, accessing lazy loading elements")
    void whenFindOrderById2_Return2Items() {
        log.info("find order by id=2");
        Optional<Order> ordersOptional = orderRepository.findById(2);

        log.info("is order present?");
        assertTrue(ordersOptional.isPresent());

        log.info("has order two orderItems? trigger lazy loading here.");
        assertThat(ordersOptional.get().getOrderItems(), hasSize(2));
    }

    @Test
    void checkOrderItemsIsPopulated() {
        OrderItem orderItem1 = testEntityManager.find(OrderItem.class, 5);
        OrderItem orderItem2 = testEntityManager.find(OrderItem.class, 6);
        OrderItem orderItem3 = testEntityManager.find(OrderItem.class, 7);
        assertNotNull(orderItem1);
        assertNotNull(orderItem2);
        assertNotNull(orderItem3);
        assertThat(orderItem1.getProductName(), is("iphone11"));
        assertThat(orderItem2.getProductName(), is("Logitech MK270"));
        assertThat(orderItem3.getProductName(), is("B&O HeadPhone"));
        assertThat(orderItem1.getOrder(), is(notNullValue()));
        assertThat(orderItem2.getOrder(), is(notNullValue()));
        assertThat(orderItem3.getOrder(), is(notNullValue()));
    }

    @Test
    @DisplayName("Join Fetch Order and OrderIterm by one trip")
    void givenOrderUUID_ReturnsInOneQuery() {
        Order order1 = testEntityManager.find(Order.class, 2);
        assertThat(order1, is(notNullValue()));

        UUID bId = order1.getBusinessId();

        log.info("find order by uuid: {} \n", bId);
        Optional<Order> ordersOptional = orderRepository.findByBusinessIdDynamic(bId, Order.class);
        assertNotNull(ordersOptional);
        assertThat(ordersOptional.isPresent(), is(true));
        Set<OrderItem> itemSet = ordersOptional.get().getOrderItems();
        assertThat(itemSet, hasSize(2));
    }

    @Test
    @DisplayName("Projecting query result to orderInfo interface")
    void findOrderByBusinessId_ProjectToOrderInfo() {
        Order targetOrder = testEntityManager.find(Order.class, 2);
        UUID bId = targetOrder.getBusinessId();

        OrderInfo orderInfo = orderRepository.findByBusinessId(bId, OrderInfo.class);
        assertNotNull(orderInfo);
    }

    @Test
    @DisplayName("Projecting query result to Entity")
    void findOrderByBusinessId_ProjectToOrderEntity() {
        Order targetOrder = testEntityManager.find(Order.class, 2);
        UUID bId = targetOrder.getBusinessId();

        Order order = orderRepository.findByBusinessId(bId, Order.class);
        assertNotNull(order);
        assertNotNull(order.getClient());
    }

}