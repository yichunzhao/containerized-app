package com.ynz.demo.containerizedapp.domain;

import com.ynz.demo.containerizedapp.shared.OrderStatus;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.Type;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.time.OffsetDateTime;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.UUID;

@Entity
@Table(name = "CLIENT_ORDERS")
@NoArgsConstructor
@Getter
@Setter
@DynamicInsert
public class Order implements IsDomain {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private int id;

    @Column(updatable = false)
    @ColumnDefault("gen_random_uuid()")
    @Type(type = "uuid-char")
    private UUID businessId;

    @Column(name = "creationDateTimeWithZone", columnDefinition = "TIMESTAMP WITH TIME ZONE")
    private OffsetDateTime createdAt;

    @Enumerated(EnumType.STRING)
    private OrderStatus orderStatus;

    @ManyToOne
    @JoinColumn(name = "FK_ClientId")
    private Client client;

    @OneToMany(mappedBy = "order", cascade = CascadeType.PERSIST)
    private Set<OrderItem> orderItems = new LinkedHashSet<>();

    public void add(@NonNull OrderItem orderItem) {
        orderItem.setOrder(this);
        this.orderItems.add(orderItem);
    }

}
