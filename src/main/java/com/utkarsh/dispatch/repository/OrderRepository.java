package com.utkarsh.dispatch.repository;

import com.utkarsh.dispatch.model.DeliveryOrder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderRepository
        extends JpaRepository<DeliveryOrder, String> {

}