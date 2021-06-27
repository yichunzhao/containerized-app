package com.ynz.demo.containerizedapp.dto;

import com.ynz.demo.containerizedapp.shared.OrderStatus;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@Getter
@NoArgsConstructor
public class OrderDto {

    @NotNull
    private OrderStatus orderStatus;

}
