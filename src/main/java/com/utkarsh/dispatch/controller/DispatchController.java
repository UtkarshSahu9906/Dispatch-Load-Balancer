package com.utkarsh.dispatch.controller;

import com.utkarsh.dispatch.dto.ApiResponseDTO;
import com.utkarsh.dispatch.dto.DispatchPlanResponseDTO;
import com.utkarsh.dispatch.dto.OrderBatchRequestDTO;
import com.utkarsh.dispatch.dto.VehicleBatchRequestDTO;
import com.utkarsh.dispatch.service.DispatchService;
import com.utkarsh.dispatch.service.OrderService;
import com.utkarsh.dispatch.service.VehicleService;

import jakarta.validation.Valid;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/dispatch")
public class DispatchController {

    private final OrderService orderService;
    private final VehicleService vehicleService;
    private final DispatchService dispatchService;

    public DispatchController(
            OrderService orderService,
            VehicleService vehicleService,
            DispatchService dispatchService
    ) {
        this.orderService = orderService;
        this.vehicleService = vehicleService;
        this.dispatchService = dispatchService;
    }

    @PostMapping("/orders")
    public ResponseEntity<ApiResponseDTO> addOrders(
            @Valid @RequestBody OrderBatchRequestDTO request
    ) {
        ApiResponseDTO response =
                orderService.saveOrders(request.getOrders());

        return ResponseEntity.ok(response);
    }
    @GetMapping("/plan")
    public ResponseEntity<DispatchPlanResponseDTO> getDispatchPlan() {

        DispatchPlanResponseDTO response =
                dispatchService.generateDispatchPlan();

        return ResponseEntity.ok(response);
    }

    @PostMapping("/vehicles")
    public ResponseEntity<ApiResponseDTO> addVehicles(
            @Valid @RequestBody VehicleBatchRequestDTO request
    ) {
        ApiResponseDTO response =
                vehicleService.saveVehicles(request.getVehicles());

        return ResponseEntity.ok(response);
    }
}