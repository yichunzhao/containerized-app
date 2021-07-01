package com.ynz.demo.containerizedapp.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.Valid;
import java.util.HashSet;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderDto implements IsDto {

    @Valid
    private Set<OrderItemDto> orderItems = new HashSet<>();

}
