package com.ynz.demo.containerizedapp.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.time.OffsetDateTime;
import java.util.UUID;

@Entity
@Table(name = "CLIENT_ORDERS")
@NoArgsConstructor
@Getter
@Setter
public class Order {

    @Id
    @GeneratedValue
    private int id;

    @Column(updatable = false, nullable = false)
    @ColumnDefault("gen_random_uuid()")
    @Type(type = "uuid-char")
    private UUID businessId;

    @Column(name = "creationDateTimeWithZone", columnDefinition = "TIME WITH TIME ZONE")
    private OffsetDateTime createdAt;

    @ManyToOne
    @JoinColumn(name = "FK_ClientId")
    private Client client;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Order order = (Order) o;
        return businessId.equals(order.businessId);
    }

    @Override
    public int hashCode() {
        return businessId.hashCode();
    }

}
