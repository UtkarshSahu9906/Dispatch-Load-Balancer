package com.utkarsh.dispatch.repository;

import com.utkarsh.dispatch.model.DeliveryOrder;
import com.utkarsh.dispatch.model.Priority;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderRepository
        extends JpaRepository<DeliveryOrder, String> {

    List<DeliveryOrder> findByPriority(Priority priority);

    List<DeliveryOrder> findByAddress(String address);

    boolean existsByOrderId(String orderId);

    List<DeliveryOrder> findByPriorityOrderByPackageWeightDesc(
            Priority priority
    );

}