package com.utkarsh.dispatch.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Entity
@Table(name = "delivery_orders")
@Getter
@Setter
@NoArgsConstructor
public class DeliveryOrder {

    @Id
    @Column(name = "order_id", nullable = false, unique = true)
    private String orderId;

    @Column(nullable = false)
    private Double latitude;

    @Column(nullable = false)
    private Double longitude;

    @Column(nullable = false)
    private String address;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal packageWeight;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Priority priority;

}