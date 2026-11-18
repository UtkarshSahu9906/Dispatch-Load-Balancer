package com.utkarsh.dispatch.dto;

import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class VehicleRequestDTO {

    @NotBlank(message = "Vehicle ID is required")
    private String vehicleId;

    @NotNull(message = "Vehicle capacity is required")
    @DecimalMin(value = "0.0", inclusive = false,
            message = "Capacity must be greater than zero")
    private BigDecimal capacity;

    @NotNull(message = "Current latitude is required")
    @DecimalMin(value = "-90.0")
    @DecimalMax(value = "90.0")
    private Double currentLatitude;

    @NotNull(message = "Current longitude is required")
    @DecimalMin(value = "-180.0")
    @DecimalMax(value = "180.0")
    private Double currentLongitude;

    @NotBlank(message = "Current address is required")
    private String currentAddress;

}