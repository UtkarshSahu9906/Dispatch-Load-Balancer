package com.utkarsh.dispatch.service;

import com.utkarsh.dispatch.dispatch.VehicleRoute;
import com.utkarsh.dispatch.dto.*;
import com.utkarsh.dispatch.model.DeliveryOrder;
import com.utkarsh.dispatch.model.Priority;
import com.utkarsh.dispatch.model.Vehicle;
import com.utkarsh.dispatch.repository.OrderRepository;
import com.utkarsh.dispatch.repository.VehicleRepository;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
public class DispatchService {

    private final OrderRepository orderRepository;
    private final VehicleRepository vehicleRepository;

    public DispatchService(
            OrderRepository orderRepository,
            VehicleRepository vehicleRepository
    ) {
        this.orderRepository = orderRepository;
        this.vehicleRepository = vehicleRepository;
    }

    @Transactional(readOnly = true)
    public DispatchPlanResponseDTO generateDispatchPlan() {

        List<DeliveryOrder> orders =
                new ArrayList<>(orderRepository.findAll());

        List<Vehicle> vehicles =
                new ArrayList<>(vehicleRepository.findAll());

        orders.sort(
                Comparator.comparingInt(
                        (DeliveryOrder order) -> getPriorityRank(order.getPriority())
                ).thenComparing(DeliveryOrder::getOrderId)
        );
        vehicles.sort(
                Comparator.comparing(Vehicle::getVehicleId)
        );

        List<VehicleRoute> routes = new ArrayList<>();

        for (Vehicle vehicle : vehicles) {
            routes.add(new VehicleRoute(vehicle));
        }

        List<UnassignedOrderDTO> unassignedOrders = new ArrayList<>();

        for (DeliveryOrder order : orders) {

            VehicleRoute bestRoute = null;
            double minimumDistance = Double.MAX_VALUE;

            for (VehicleRoute route : routes) {

                if (!route.canAcceptOrder(order)) {
                    continue;
                }

                double distance =
                        route.calculateAdditionalDistance(order);

                if (distance < minimumDistance) {
                    minimumDistance = distance;
                    bestRoute = route;
                }
            }

            if (bestRoute != null) {

                bestRoute.assignOrder(order);

            } else {

                unassignedOrders.add(
                        new UnassignedOrderDTO(
                                order.getOrderId(),
                                "No vehicle has sufficient remaining capacity"
                        )
                );
            }
        }

        List<DispatchPlanDTO> dispatchPlans = new ArrayList<>();

        for (VehicleRoute route : routes) {

            Vehicle vehicle = route.getVehicle();

            List<OrderResponseDTO> assignedOrders = new ArrayList<>();

            for (DeliveryOrder order : route.getAssignedOrders()) {

                assignedOrders.add(
                        new OrderResponseDTO(
                                order.getOrderId(),
                                order.getLatitude(),
                                order.getLongitude(),
                                order.getAddress(),
                                order.getPackageWeight(),
                                order.getPriority()
                        )
                );
            }

            dispatchPlans.add(
                    new DispatchPlanDTO(
                            vehicle.getVehicleId(),
                            vehicle.getCurrentAddress(),
                            vehicle.getCapacity(),
                            route.getTotalLoad(),
                            route.getTotalDistance(),
                            assignedOrders
                    )
            );
        }

        return new DispatchPlanResponseDTO(
                dispatchPlans,
                unassignedOrders
        );
    }

    private int getPriorityRank(Priority priority) {

        return switch (priority) {
            case HIGH -> 1;
            case MEDIUM -> 2;
            case LOW -> 3;
        };
    }
}