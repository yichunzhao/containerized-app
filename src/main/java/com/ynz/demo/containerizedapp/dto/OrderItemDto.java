package com.ynz.demo.containerizedapp.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.PositiveOrZero;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderItemDto implements IsDto {

    @NotBlank(message = "Order item must contain a product name")
    private String productName;

    @PositiveOrZero(message = "amount must >=0")
    private int amount;

}
