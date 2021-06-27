package com.ynz.demo.containerizedapp.repository;

import com.ynz.demo.containerizedapp.domain.Order;
import com.ynz.demo.containerizedapp.dto.OrderInfo;
import org.springframework.data.repository.CrudRepository;

import java.util.UUID;

public interface OrderRepository extends CrudRepository<Order, Integer> {

    OrderInfo findByBusinessId(UUID businessId);

}
