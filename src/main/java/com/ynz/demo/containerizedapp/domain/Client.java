package com.ynz.demo.containerizedapp.domain;


import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.Setter;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.validation.constraints.Email;
import java.util.LinkedHashSet;
import java.util.Set;

@Entity
@NoArgsConstructor
@Getter
@Setter
public class Client implements IsDomain {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private int id;

    @Column(nullable = false)
    private String name;

    @Email
    @Column(nullable = false, unique = true)
    private String email;

    @OneToMany(mappedBy = "client", targetEntity = Order.class, cascade = CascadeType.PERSIST)
    private Set<Order> orders = new LinkedHashSet<>();

    public void addOrder(@NonNull Order order) {
        order.setClient(this);
        orders.add(order);
    }

}
