package com.utkarsh.dispatch.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.math.BigDecimal;
import java.util.List;

@Getter
@AllArgsConstructor
public class DispatchPlanDTO {

    private String vehicleId;

    private String currentAddress;

    private BigDecimal capacity;

    private BigDecimal totalLoad;

    private Double totalDistance;

    private List<OrderResponseDTO> assignedOrders;

}