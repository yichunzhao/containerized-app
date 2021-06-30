package com.ynz.demo.containerizedapp.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
@AllArgsConstructor
public class OrderItemDto implements IsDto {

    @NotBlank
    private String productName;

    @NotBlank
    private int amount;
}
