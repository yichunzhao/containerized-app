package com.ynz.demo.containerizedapp.dto.mapper;

import com.ynz.demo.containerizedapp.domain.OrderItem;
import com.ynz.demo.containerizedapp.dto.OrderItemDto;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor(staticName = "instance")
public class OrderItemMapper implements Invertible<OrderItem, OrderItemDto> {

    @Override
    public OrderItemDto mapToDto(@NonNull OrderItem orderItem) {
        return new OrderItemDto(orderItem.getProductName(), orderItem.getAmount());
    }

    @Override
    public OrderItem mapToEntity(@NonNull OrderItemDto orderItemDto) {
        OrderItem orderItem = new OrderItem();
        orderItem.setAmount(orderItemDto.getAmount());
        orderItem.setProductName(orderItemDto.getProductName());
        return orderItem;
    }

}
