package com.ynz.demo.containerizedapp.repository;

import com.ynz.demo.containerizedapp.domain.OrderItem;
import org.springframework.data.repository.CrudRepository;

public interface OrderItemRepository extends CrudRepository<OrderItem, Integer> {

}
