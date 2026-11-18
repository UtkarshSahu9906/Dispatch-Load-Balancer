package com.utkarsh.dispatch.dispatch;

import com.utkarsh.dispatch.model.DeliveryOrder;
import com.utkarsh.dispatch.model.Vehicle;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class VehicleRoute {

    private final Vehicle vehicle;

    private final List<DeliveryOrder> assignedOrders;

    private BigDecimal totalLoad;

    private double totalDistance;

    private double lastLatitude;

    private double lastLongitude;

    public VehicleRoute(Vehicle vehicle) {

        this.vehicle = vehicle;

        this.assignedOrders = new ArrayList<>();

        this.totalLoad = BigDecimal.ZERO;

        this.totalDistance = 0.0;

        this.lastLatitude = vehicle.getCurrentLatitude();

        this.lastLongitude = vehicle.getCurrentLongitude();
    }

    public boolean canAcceptOrder(DeliveryOrder order) {

        BigDecimal updatedLoad =
                totalLoad.add(order.getPackageWeight());

        return updatedLoad.compareTo(vehicle.getCapacity()) <= 0;
    }

    public double calculateAdditionalDistance(DeliveryOrder order) {

        return com.utkarsh.dispatch.util.DistanceCalculator.calculateDistance(
                lastLatitude,
                lastLongitude,
                order.getLatitude(),
                order.getLongitude()
        );
    }

    public void assignOrder(DeliveryOrder order) {

        double distance = calculateAdditionalDistance(order);

        totalDistance += distance;

        totalLoad = totalLoad.add(order.getPackageWeight());

        assignedOrders.add(order);

        lastLatitude = order.getLatitude();

        lastLongitude = order.getLongitude();
    }

    public Vehicle getVehicle() {
        return vehicle;
    }

    public List<DeliveryOrder> getAssignedOrders() {
        return assignedOrders;
    }

    public BigDecimal getTotalLoad() {
        return totalLoad;
    }

    public double getTotalDistance() {
        return totalDistance;
    }
}