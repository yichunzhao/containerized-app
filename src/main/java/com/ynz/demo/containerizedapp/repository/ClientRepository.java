package com.ynz.demo.containerizedapp.repository;

import com.ynz.demo.containerizedapp.domain.Client;
import com.ynz.demo.containerizedapp.domain.Order;
import com.ynz.demo.containerizedapp.dto.ClientInfo;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface ClientRepository extends CrudRepository<Client, Integer> {

    @Query("SELECT o FROM Client c JOIN c.orders o WHERE c.email = :email")
    List<Order> findClientOrdersByEmail(@Param("email") String email);

    Optional<ClientInfo> findByEmail(String email);

    @Query("SELECT c FROM Client c JOIN c.orders o WHERE o.businessId = :businessId")
    Optional<Client> findClientByOrderBusinessId(UUID businessId);

}
