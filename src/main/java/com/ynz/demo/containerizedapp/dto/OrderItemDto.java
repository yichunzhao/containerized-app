package com.ynz.demo.containerizedapp.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class OrderItemDto {
    private String productName;
    private int amount;
}
