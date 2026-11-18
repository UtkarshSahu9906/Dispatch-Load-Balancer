package com.utkarsh.dispatch.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class UnassignedOrderDTO {

    private String orderId;

    private String reason;

}