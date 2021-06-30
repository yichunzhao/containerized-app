package com.ynz.demo.containerizedapp.dto.projection;

import com.ynz.demo.containerizedapp.shared.OrderStatus;

import java.time.OffsetDateTime;
import java.util.UUID;

public interface OrderInfo {
    UUID getBusinessId();

    OffsetDateTime getCreatedAt();

    OrderStatus getOrderStatus();

}
