package com.ynz.demo.containerizedapp.repository;

import com.ynz.demo.containerizedapp.domain.Client;
import com.ynz.demo.containerizedapp.domain.Order;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ClientRepository extends CrudRepository<Client, Integer> {

    @Query("SELECT o from Client c JOIN c.orders o WHERE c.email = :email")
    List<Order> findClientOrdersByEmail(@Param("email") String email);

    Client findByEmail(String email);

}
