package com.ynz.demo.containerizedapp.repository;

import com.ynz.demo.containerizedapp.domain.Order;
import org.springframework.data.repository.CrudRepository;

public interface OrderRepository extends CrudRepository<Order, Integer> {
}
