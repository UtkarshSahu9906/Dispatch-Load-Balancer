package com.utkarsh.dispatch.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class OrderBatchRequestDTO {

    @NotEmpty(message = "At least one order is required")
    private List<@Valid OrderRequestDTO> orders;
}