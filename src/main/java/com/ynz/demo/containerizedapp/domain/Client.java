package com.ynz.demo.containerizedapp.domain;


import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
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

    private String name;

    @OneToMany(mappedBy = "client", targetEntity = Order.class)
    private Set<Order> orders = new HashSet<>();

}
