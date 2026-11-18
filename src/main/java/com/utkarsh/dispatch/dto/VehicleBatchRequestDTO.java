package com.utkarsh.dispatch.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class VehicleBatchRequestDTO {

    @NotEmpty(message = "At least one vehicle is required")
    private List<@Valid VehicleRequestDTO> vehicles;
}