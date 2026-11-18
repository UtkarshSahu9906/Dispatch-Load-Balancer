package com.utkarsh.dispatch.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class DispatchPlanResponseDTO {

    private List<DispatchPlanDTO> dispatchPlan;

    private List<UnassignedOrderDTO> unassignedOrders;

}