package com.utkarsh.dispatch.service;

import com.utkarsh.dispatch.dto.ApiResponseDTO;
import com.utkarsh.dispatch.dto.OrderRequestDTO;
import com.utkarsh.dispatch.model.DeliveryOrder;
import com.utkarsh.dispatch.repository.OrderRepository;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
public class OrderService {

    private final OrderRepository orderRepository;

    public OrderService(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    @Transactional
    public ApiResponseDTO saveOrders(List<OrderRequestDTO> orderRequests) {

        Set<String> orderIds = new HashSet<>();

        for (OrderRequestDTO request : orderRequests) {
            if (!orderIds.add(request.getOrderId())) {
                throw new IllegalArgumentException(
                        "Duplicate order ID in request: " + request.getOrderId()
                );
            }
        }

        List<DeliveryOrder> existingOrders =
                orderRepository.findAllById(orderIds);

        Map<String, DeliveryOrder> existingOrderMap = new HashMap<>();

        for (DeliveryOrder order : existingOrders) {
            existingOrderMap.put(order.getOrderId(), order);
        }

        List<DeliveryOrder> ordersToSave = new ArrayList<>();

        for (OrderRequestDTO request : orderRequests) {

            DeliveryOrder order = existingOrderMap.get(request.getOrderId());

            if (order == null) {
                order = new DeliveryOrder();
                order.setOrderId(request.getOrderId());
            }

            order.setLatitude(request.getLatitude());
            order.setLongitude(request.getLongitude());
            order.setAddress(request.getAddress());
            order.setPackageWeight(request.getPackageWeight());
            order.setPriority(request.getPriority());

            ordersToSave.add(order);
        }

        orderRepository.saveAll(ordersToSave);

        return new ApiResponseDTO(
                "Delivery orders accepted.",
                "success"
        );
    }
}