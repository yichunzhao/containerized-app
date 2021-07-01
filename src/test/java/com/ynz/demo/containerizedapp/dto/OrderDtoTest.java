package com.ynz.demo.containerizedapp.dto;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.util.Set;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasSize;


@Slf4j
class OrderDtoTest {

    @Test
    void validateBeanProgrammatically() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        Validator validator = factory.getValidator();

        OrderDto orderDto = new OrderDto();

        OrderItemDto orderItemDto = new OrderItemDto();
        orderItemDto.setProductName("iphone11");
        orderItemDto.setAmount(-1);

        orderDto.add(orderItemDto);

        Set<ConstraintViolation<OrderDto>> violations = validator.validate(orderDto);
        violations.forEach(violation ->
                log.info("root class: {}, property path: {}, error message: {}", violation.getRootBeanClass().getName(),
                        violation.getPropertyPath(), violation.getMessage()));

        assertThat(violations, hasSize(1));
    }

}