import json
import random
import time
from urllib.request import Request, urlopen

BASE_URL = "http://localhost:8080/api/dispatch"

random.seed(42)


def send_request(endpoint, data=None):
    url = f"{BASE_URL}/{endpoint}"

    headers = {"Content-Type": "application/json"}

    request = Request(
        url,
        data=json.dumps(data).encode("utf-8") if data else None,
        headers=headers,
        method="POST" if data else "GET"
    )

    start = time.perf_counter()

    with urlopen(request) as response:
        result = json.loads(response.read().decode("utf-8"))

    elapsed = time.perf_counter() - start

    return result, elapsed


# Generate 1000 delivery orders
orders = []

for i in range(1, 1001):
    orders.append({
        "orderId": f"PERF-ORD-{i:04d}",
        "latitude": round(random.uniform(28.4, 28.8), 6),
        "longitude": round(random.uniform(76.8, 77.5), 6),
        "address": f"Performance Test Address {i}",
        "packageWeight": round(random.uniform(1, 3), 2),
        "priority": random.choice(["HIGH", "MEDIUM", "LOW"])
    })


# Generate 50 vehicles
vehicles = []

for i in range(1, 51):
    vehicles.append({
        "vehicleId": f"PERF-VEH-{i:03d}",
        "capacity": 100,
        "currentLatitude": round(random.uniform(28.4, 28.8), 6),
        "currentLongitude": round(random.uniform(76.8, 77.5), 6),
        "currentAddress": f"Vehicle Starting Point {i}"
    })


print("Uploading 1000 orders...")

_, order_time = send_request("orders", {"orders": orders})

print(f"Order upload time: {order_time:.3f} seconds")


print("\nUploading 50 vehicles...")

_, vehicle_time = send_request("vehicles", {"vehicles": vehicles})

print(f"Vehicle upload time: {vehicle_time:.3f} seconds")


print("\nGenerating dispatch plan...")

plan, dispatch_time = send_request("plan")

print(f"Dispatch generation time: {dispatch_time:.3f} seconds")


# Calculate assignment statistics
performance_order_ids = {
    order["orderId"] for order in orders
}
# Filter only performance-test orders
capacity_errors = []

for vehicle in plan["dispatchPlan"]:
    if vehicle["totalLoad"] > vehicle["capacity"]:
        capacity_errors.append({
            "vehicleId": vehicle["vehicleId"],
            "capacity": vehicle["capacity"],
            "totalLoad": vehicle["totalLoad"]
        })

print("\n========== CAPACITY VALIDATION ==========")

if capacity_errors:
    print("FAILED: Vehicle capacity exceeded!")
    print(capacity_errors)
else:
    print("PASSED: No vehicle exceeds its capacity.")

print("=========================================")

priority_rank = {
    "HIGH": 1,
    "MEDIUM": 2,
    "LOW": 3
}

assigned_priorities = []

for vehicle in plan["dispatchPlan"]:
    for order in vehicle["assignedOrders"]:
        if order["orderId"] in performance_order_ids:
            assigned_priorities.append(
                priority_rank[order["priority"]]
            )

is_priority_sorted = (
        assigned_priorities == sorted(assigned_priorities)
)

print("\n========== PRIORITY VALIDATION ==========")

if is_priority_sorted:
    print("PASSED: Orders are processed by priority.")
else:
    print("Priority sequence is not globally sorted.")

print("=========================================")

