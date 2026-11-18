package com.utkarsh.dispatch.service;

import com.utkarsh.dispatch.dto.DispatchPlanResponseDTO;
import com.utkarsh.dispatch.model.DeliveryOrder;
import com.utkarsh.dispatch.model.Priority;
import com.utkarsh.dispatch.model.Vehicle;
import com.utkarsh.dispatch.repository.OrderRepository;
import com.utkarsh.dispatch.repository.VehicleRepository;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DispatchServiceTest {

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private VehicleRepository vehicleRepository;

    @InjectMocks
    private DispatchService dispatchService;
    @Test
    void shouldMarkAllOrdersUnassignedWhenNoVehiclesAvailable() {

        DeliveryOrder order = createOrder(
                "ORD003", "5", Priority.HIGH
        );

        when(orderRepository.findAll())
                .thenReturn(List.of(order));

        when(vehicleRepository.findAll())
                .thenReturn(List.of());

        DispatchPlanResponseDTO response =
                dispatchService.generateDispatchPlan();

        assertTrue(response.getDispatchPlan().isEmpty());

        assertEquals(1, response.getUnassignedOrders().size());

        assertEquals(
                "ORD003",
                response.getUnassignedOrders().get(0).getOrderId()
        );
    }

    @Test
    void shouldAssignHighPriorityOrderBeforeLowPriorityOrder() {

        // Arrange: Create a vehicle with 10 kg capacity
        Vehicle vehicle = new Vehicle();
        vehicle.setVehicleId("VEH001");
        vehicle.setCapacity(new BigDecimal("10"));
        vehicle.setCurrentLatitude(28.6139);
        vehicle.setCurrentLongitude(77.2090);
        vehicle.setCurrentAddress("Delhi");

        // Create HIGH priority order
        DeliveryOrder highOrder = createOrder(
                "ORD001", "5", Priority.HIGH
        );

        // Create LOW priority order
        DeliveryOrder lowOrder = createOrder(
                "ORD002", "10", Priority.LOW
        );

        when(orderRepository.findAll())
                .thenReturn(List.of(lowOrder, highOrder));

        when(vehicleRepository.findAll())
                .thenReturn(List.of(vehicle));

        // Act
        DispatchPlanResponseDTO response =
                dispatchService.generateDispatchPlan();

        // Assert
        assertEquals(1, response.getDispatchPlan().size());

        assertEquals(
                "ORD001",
                response.getDispatchPlan()
                        .get(0)
                        .getAssignedOrders()
                        .get(0)
                        .getOrderId()
        );

        assertEquals(1, response.getUnassignedOrders().size());

        assertEquals(
                "ORD002",
                response.getUnassignedOrders().get(0).getOrderId()
        );
    }

    private DeliveryOrder createOrder(
            String orderId,
            String weight,
            Priority priority
    ) {
        DeliveryOrder order = new DeliveryOrder();

        order.setOrderId(orderId);
        order.setLatitude(28.6139);
        order.setLongitude(77.2090);
        order.setAddress("Delhi");
        order.setPackageWeight(new BigDecimal(weight));
        order.setPriority(priority);

        return order;
    }
}