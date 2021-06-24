package com.ynz.demo.containerizedapp.repository;

import com.ynz.demo.containerizedapp.domain.Client;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Set;

@Repository
public interface UserRepository extends CrudRepository<Client, Integer> {
    //Set<Client> findByClientOrders(String name);
}
