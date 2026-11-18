package com.utkarsh.dispatch.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Entity
@Table(name = "vehicles")
@Getter
@Setter
@NoArgsConstructor
public class Vehicle {

    @Id
    @Column(name = "vehicle_id", nullable = false, unique = true)
    private String vehicleId;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal capacity;

    @Column(nullable = false)
    private Double currentLatitude;

    @Column(nullable = false)
    private Double currentLongitude;

    @Column(nullable = false)
    private String currentAddress;

}