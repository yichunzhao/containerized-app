package com.ynz.demo.containerizedapp.repository;

import com.ynz.demo.containerizedapp.domain.Order;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.Optional;
import java.util.UUID;

public interface OrderRepository extends CrudRepository<Order, Integer> {

    //dynamic projection
    @Query("SELECT o FROM Order o JOIN FETCH o.orderItems WHERE o.businessId=:businessId")
    <T> Optional<T> findByBusinessIdDynamic(@Param("businessId") UUID businessId, Class<T> type);

    <T> T findByBusinessId(UUID businessId, Class<T> type);

}
