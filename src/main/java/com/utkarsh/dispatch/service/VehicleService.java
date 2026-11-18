package com.utkarsh.dispatch.service;

import com.utkarsh.dispatch.dto.ApiResponseDTO;
import com.utkarsh.dispatch.dto.VehicleRequestDTO;
import com.utkarsh.dispatch.model.Vehicle;
import com.utkarsh.dispatch.repository.VehicleRepository;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
public class VehicleService {

    private final VehicleRepository vehicleRepository;

    public VehicleService(VehicleRepository vehicleRepository) {
        this.vehicleRepository = vehicleRepository;
    }

    @Transactional
    public ApiResponseDTO saveVehicles(List<VehicleRequestDTO> vehicleRequests) {

        Set<String> vehicleIds = new HashSet<>();

        for (VehicleRequestDTO request : vehicleRequests) {
            if (!vehicleIds.add(request.getVehicleId())) {
                throw new IllegalArgumentException(
                        "Duplicate vehicle ID in request: " + request.getVehicleId()
                );
            }
        }

        // Find vehicles that already exist.
        List<Vehicle> existingVehicles =
                vehicleRepository.findAllById(vehicleIds);

        Map<String, Vehicle> existingVehicleMap = new HashMap<>();

        for (Vehicle vehicle : existingVehicles) {
            existingVehicleMap.put(vehicle.getVehicleId(), vehicle);
        }

        List<Vehicle> vehiclesToSave = new ArrayList<>();

        for (VehicleRequestDTO request : vehicleRequests) {

            Vehicle vehicle = existingVehicleMap.get(request.getVehicleId());

            if (vehicle == null) {
                vehicle = new Vehicle();
                vehicle.setVehicleId(request.getVehicleId());
            }

            vehicle.setCapacity(request.getCapacity());
            vehicle.setCurrentLatitude(request.getCurrentLatitude());
            vehicle.setCurrentLongitude(request.getCurrentLongitude());
            vehicle.setCurrentAddress(request.getCurrentAddress());

            vehiclesToSave.add(vehicle);
        }

        vehicleRepository.saveAll(vehiclesToSave);

        return new ApiResponseDTO(
                "Vehicles accepted.",
                "success"
        );
    }
}