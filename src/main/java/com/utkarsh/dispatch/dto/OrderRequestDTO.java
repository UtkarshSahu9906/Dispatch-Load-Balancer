package com.utkarsh.dispatch.dto;

import com.utkarsh.dispatch.model.Priority;
import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class OrderRequestDTO {

    @NotBlank(message = "Order ID is required")
    private String orderId;

    @NotNull(message = "Latitude is required")
    @DecimalMin(value = "-90.0")
    @DecimalMax(value = "90.0")
    private Double latitude;

    @NotNull(message = "Longitude is required")
    @DecimalMin(value = "-180.0")
    @DecimalMax(value = "180.0")
    private Double longitude;

    @NotBlank(message = "Address is required")
    private String address;

    @NotNull(message = "Package weight is required")
    @DecimalMin(value = "0.0", inclusive = false,
            message = "Package weight must be greater than zero")
    private BigDecimal packageWeight;

    @NotNull(message = "Priority is required")
    private Priority priority;

}