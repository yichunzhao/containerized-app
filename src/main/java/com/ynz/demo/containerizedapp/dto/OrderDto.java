package com.ynz.demo.containerizedapp.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderDto implements IsDto {

    private Set<OrderItemDto> orderItems = new HashSet<>();

}
