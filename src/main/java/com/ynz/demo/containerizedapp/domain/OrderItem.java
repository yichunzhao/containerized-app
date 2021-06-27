package com.ynz.demo.containerizedapp.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;


@Entity
@Getter
@Setter
@NoArgsConstructor
public class OrderItem {

    @Id
    @GeneratedValue
    private Integer id;

    @Column(nullable = false)
    private String productName;

    @Column(nullable = false)
    private int amount;

    //navigation attributes
    @ManyToOne
    @JoinColumn(name = "FK_Order_Id")
    private Order order;

    @Override
    public int hashCode() {
        return 1245;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        OrderItem orderItem = (OrderItem) o;
        return this.getId().equals(orderItem.getId());
    }

}
