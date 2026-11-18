package com.utkarsh.dispatch.repository;

import com.utkarsh.dispatch.model.Vehicle;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

@Repository
public interface VehicleRepository
        extends JpaRepository<Vehicle, String> {

    List<Vehicle> findByCapacityGreaterThanEqual(
            BigDecimal capacity
    );

    boolean existsByVehicleId(String vehicleId);

    List<Vehicle> findByCurrentAddress(String address);

}