package com.ynz.demo.containerizedapp.dto.mapper;

import com.ynz.demo.containerizedapp.domain.Order;
import com.ynz.demo.containerizedapp.domain.OrderItem;
import com.ynz.demo.containerizedapp.dto.OrderDto;
import com.ynz.demo.containerizedapp.dto.OrderItemDto;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import java.util.Set;

import static java.util.stream.Collectors.toSet;

@RequiredArgsConstructor(staticName = "instance")
public class OrderMapper implements EntityToDto<Order, OrderDto> {

    @Override
    public OrderDto mapToDto(@NonNull Order order) {
        OrderItemMapper mapper = OrderItemMapper.instance();
        Set<OrderItemDto> itemDtoSet = order.getOrderItems().stream().map(mapper::mapToDto).collect(toSet());

        return new OrderDto(order.getOrderStatus(), itemDtoSet);
    }

    @Override
    public Order mapToEntity(@NonNull OrderDto orderDto) {
        OrderItemMapper mapper = OrderItemMapper.instance();
        Set<OrderItem> orderItemSet = orderDto.getOrderItems().stream().map(mapper::mapToEntity).collect(toSet());

        Order order = new Order();
        order.setOrderStatus(orderDto.getOrderStatus());
        orderItemSet.forEach(order::add);

        return order;
    }

}
