package com.utkarsh.dispatch.service;

import com.utkarsh.dispatch.dto.OrderRequestDTO;
import com.utkarsh.dispatch.model.Priority;
import com.utkarsh.dispatch.repository.OrderRepository;

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
class OrderServiceTest {

    @Mock
    private OrderRepository orderRepository;

    @InjectMocks
    private OrderService orderService;

    @Test
    void shouldRejectDuplicateOrderIdsInSameRequest() {

        OrderRequestDTO first = createOrder("ORD001");
        OrderRequestDTO second = createOrder("ORD001");

        List<OrderRequestDTO> orders = List.of(first, second);

        assertThrows(
                IllegalArgumentException.class,
                () -> orderService.saveOrders(orders)
        );

        verify(orderRepository, never()).saveAll(anyList());
    }

    private OrderRequestDTO createOrder(String orderId) {

        OrderRequestDTO order = new OrderRequestDTO();

        order.setOrderId(orderId);
        order.setLatitude(28.6139);
        order.setLongitude(77.2090);
        order.setAddress("Delhi");
        order.setPackageWeight(new BigDecimal("5"));
        order.setPriority(Priority.HIGH);

        return order;
    }
}