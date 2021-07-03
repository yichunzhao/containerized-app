package com.ynz.demo.containerizedapp.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.util.Objects;


@Entity
@Table(name = "Order_Items")
@Getter
@Setter
@NoArgsConstructor
public class OrderItem implements IsDomain {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
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
        return 12345;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        OrderItem orderItem = (OrderItem) o;
        return amount == orderItem.amount && Objects.equals(id, orderItem.id) && Objects.equals(productName, orderItem.productName) && Objects.equals(order, orderItem.order);
    }

}
