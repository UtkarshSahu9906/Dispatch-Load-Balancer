package com.utkarsh.dispatch.dto;

import com.utkarsh.dispatch.model.Priority;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.math.BigDecimal;

@Getter
@AllArgsConstructor
public class OrderResponseDTO {

    private String orderId;

    private Double latitude;

    private Double longitude;

    private String address;

    private BigDecimal packageWeight;

    private Priority priority;

}