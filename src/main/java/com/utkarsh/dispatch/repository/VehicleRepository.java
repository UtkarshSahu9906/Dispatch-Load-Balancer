package com.utkarsh.dispatch.repository;

import com.utkarsh.dispatch.model.Vehicle;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VehicleRepository
        extends JpaRepository<Vehicle, String> {

}