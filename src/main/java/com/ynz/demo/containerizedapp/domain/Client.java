package com.ynz.demo.containerizedapp.domain;


import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.Email;
import java.util.HashSet;
import java.util.Set;

@Entity
@NoArgsConstructor
@Getter
@Setter
public class Client {

    @Id
    @GeneratedValue
    private int id;

    @Column(nullable = false)
    private String name;

    @Email
    @Column(nullable = false, unique = true)
    private String email;

    @OneToMany(mappedBy = "client", targetEntity = Order.class)
    private Set<Order> orders = new HashSet<>();

    public void addOrder(Order order) {
        order.setClient(this);
        orders.add(order);
    }

}
