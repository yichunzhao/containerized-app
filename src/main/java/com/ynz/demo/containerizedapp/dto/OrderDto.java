package com.ynz.demo.containerizedapp.dto;

import com.ynz.demo.containerizedapp.shared.OrderStatus;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.HashSet;
import java.util.Set;

@Data
@AllArgsConstructor
public class OrderDto {

    private OrderStatus orderStatus;

    private Set<OrderItemDto> orderItems = new HashSet<>();

}
