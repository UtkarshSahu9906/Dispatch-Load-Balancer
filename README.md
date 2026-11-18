# Dispatch Load Balancer

A Spring Boot backend application that automatically assigns delivery orders to available vehicles based on vehicle capacity, order priority, and geographical distance.

The application uses the **Haversine formula** to calculate distances between delivery locations and vehicle starting positions. It follows a greedy allocation strategy to generate dispatch plans while respecting vehicle capacity and order priority.

## Features

- Create and update delivery orders.
- Create and update vehicles.
- Priority-based order allocation (HIGH, MEDIUM, LOW).
- Vehicle capacity validation.
- Distance calculation using the Haversine formula.
- Automatic dispatch plan generation.
- Identification of unassigned orders with reasons.
- REST API with centralized exception handling.
- Request validation using Jakarta Validation.
- Batch database operations.
- Unit testing with JUnit and Mockito.
- Performance testing with a sample workload of 1,000 orders and 50 vehicles.

## Technology Stack

| Technology | Purpose |
|---|---|
| Java 21 | Backend programming language |
| Spring Boot 4.1.2 | Application framework |
| Spring Data JPA | Database interaction |
| MySQL | Relational database |
| Maven | Build and dependency management |
| Lombok | Boilerplate code reduction |
| Jakarta Validation | Request validation |
| JUnit 5 | Unit testing |
| Mockito | Mocking dependencies |
| Python 3 | Performance testing script |

## Project Structure

```text
dispatch-load-balancer/
│
├── src/
│   ├── main/
│   │   ├── java/com/utkarsh/dispatch/
│   │   │   ├── controller/
│   │   │   │   └── DispatchController.java
│   │   │   │
│   │   │   ├── dispatch/
│   │   │   │   └── VehicleRoute.java
│   │   │   │
│   │   │   ├── dto/
│   │   │   │   ├── ApiResponseDTO.java
│   │   │   │   ├── DispatchPlanDTO.java
│   │   │   │   ├── DispatchPlanResponseDTO.java
│   │   │   │   ├── OrderBatchRequestDTO.java
│   │   │   │   ├── OrderRequestDTO.java
│   │   │   │   ├── OrderResponseDTO.java
│   │   │   │   ├── UnassignedOrderDTO.java
│   │   │   │   ├── VehicleBatchRequestDTO.java
│   │   │   │   └── VehicleRequestDTO.java
│   │   │   │
│   │   │   ├── exception/
│   │   │   │   └── GlobalExceptionHandler.java
│   │   │   │
│   │   │   ├── model/
│   │   │   │   ├── DeliveryOrder.java
│   │   │   │   ├── Priority.java
│   │   │   │   └── Vehicle.java
│   │   │   │
│   │   │   ├── repository/
│   │   │   │   ├── OrderRepository.java
│   │   │   │   └── VehicleRepository.java
│   │   │   │
│   │   │   ├── service/
│   │   │   │   ├── DispatchService.java
│   │   │   │   ├── OrderService.java
│   │   │   │   └── VehicleService.java
│   │   │   │
│   │   │   ├── util/
│   │   │   │   └── DistanceCalculator.java
│   │   │   │
│   │   │   └── DispatchLoadBalancerApplication.java
│   │   │
│   │   └── resources/
│   │       └── application.yaml
│   │
│   └── test/
│       └── java/com/utkarsh/dispatch/
│           ├── exception/
│           ├── service/
│           ├── util/
│           └── DispatchLoadBalancerApplicationTests.java
│
├── .env.example
├── .gitignore
├── performance-test.py
├── pom.xml
└── README.md
```

## Prerequisites

Make sure the following tools are installed:

- Java 21
- Maven
- MySQL Server
- Git
- Python 3 (for performance testing)

Verify your installations:

```bash
java -version
mvn -version
mysql --version
python3 --version
```

## Database Configuration

### 1. Create the Database

Open MySQL and execute:

```sql
CREATE DATABASE dispatch_db;
```

### 2. Configure Environment Variables

The project includes an `.env.example` file containing the required database configuration.

Copy it to create your local environment file:

```bash
cp .env.example .env
```

Update the `.env` file with your MySQL credentials:

```env
DB_URL=jdbc:mysql://localhost:3306/dispatch_db
DB_USERNAME=root
DB_PASSWORD=your_mysql_password
```

The application loads these environment variables through `application.yaml`.

**Important:** Never commit your actual `.env` file or database credentials to GitHub.

## Running the Application

### 1. Clone the Repository

```bash
git clone <YOUR_GITHUB_REPOSITORY_URL>
```

### 2. Navigate to the Project Directory

```bash
cd dispatch-load-balancer
```

### 3. Build the Application

```bash
mvn clean install
```

### 4. Run the Application

```bash
mvn spring-boot:run
```

The application runs at:

```text
http://localhost:8080
```

## REST API Documentation

**Base URL:**

```text
http://localhost:8080/api/dispatch
```

### 1. Add or Update Delivery Orders

**Endpoint:** `POST /api/dispatch/orders`

**Description:** Accepts a batch of delivery orders. Existing order IDs are updated, while new order IDs are inserted.

**Example Request:**

```json
{
  "orders": [
    {
      "orderId": "ORD001",
      "latitude": 28.6139,
      "longitude": 77.2090,
      "address": "Connaught Place, Delhi",
      "packageWeight": 10.0,
      "priority": "HIGH"
    },
    {
      "orderId": "ORD002",
      "latitude": 28.5355,
      "longitude": 77.3910,
      "address": "Noida Sector 18",
      "packageWeight": 15.0,
      "priority": "MEDIUM"
    }
  ]
}
```

**Example Response:**

```json
{
  "message": "Delivery orders accepted.",
  "status": "success"
}
```

### 2. Add or Update Vehicles

**Endpoint:** `POST /api/dispatch/vehicles`

**Description:** Accepts a batch of vehicles. Existing vehicle IDs are updated, while new vehicle IDs are inserted.

**Example Request:**

```json
{
  "vehicles": [
    {
      "vehicleId": "VEH001",
      "capacity": 100.0,
      "currentLatitude": 28.6139,
      "currentLongitude": 77.2090,
      "currentAddress": "Delhi"
    },
    {
      "vehicleId": "VEH002",
      "capacity": 150.0,
      "currentLatitude": 28.5355,
      "currentLongitude": 77.3910,
      "currentAddress": "Noida"
    }
  ]
}
```

**Example Response:**

```json
{
  "message": "Vehicles accepted.",
  "status": "success"
}
```

### 3. Generate Dispatch Plan

**Endpoint:** `GET /api/dispatch/plan`

**Description:** Generates a dispatch plan using the stored orders and available vehicles.

**Example Response:**

```json
{
  "dispatchPlan": [
    {
      "vehicleId": "VEH001",
      "currentAddress": "Delhi",
      "capacity": 100.0,
      "totalLoad": 10.0,
      "totalDistance": 0.0,
      "assignedOrders": [
        {
          "orderId": "ORD001",
          "latitude": 28.6139,
          "longitude": 77.2090,
          "address": "Connaught Place, Delhi",
          "packageWeight": 10.0,
          "priority": "HIGH"
        }
      ]
    }
  ],
  "unassignedOrders": []
}
```

The example is illustrative. Actual assignments and distances depend on the orders and vehicles stored in the database.

### HTTP Status Codes

| Status Code | Description |
|---|---|
| 200 OK | Request processed successfully |
| 400 Bad Request | Invalid request or validation failure |
| 500 Internal Server Error | Unexpected server-side error |

## Dispatch Algorithm

The application follows a **greedy allocation approach** to assign delivery orders to vehicles.

### Allocation Steps

1. Fetch available delivery orders and vehicles from the database.
2. Sort orders by priority:
   - HIGH
   - MEDIUM
   - LOW
3. Sort orders with equal priority by order ID.
4. Check each vehicle's remaining capacity.
5. Calculate the additional travel distance for each eligible vehicle.
6. Select the eligible vehicle with the smallest additional travel distance.
7. Update the selected vehicle's route and total load.
8. If no vehicle has sufficient capacity, mark the order as unassigned.

### Distance Calculation — Haversine Formula

The application uses the Haversine formula to calculate geographical distance between two latitude-longitude coordinates.

The formula calculates the great-circle distance between two points on a sphere.

Earth's mean radius is taken as approximately:

```text
6371 kilometres
```

The calculated distance is used to estimate the additional travel distance when assigning an order to a vehicle.

**Note:** The greedy algorithm makes locally efficient assignments but does not guarantee a globally optimal allocation or route.

## Validation

The application uses Jakarta Validation to validate incoming API requests.

### Order Validation

- Order ID is required.
- Latitude must be between -90 and 90.
- Longitude must be between -180 and 180.
- Address is required.
- Package weight must be positive.
- Priority must be provided.

### Vehicle Validation

- Vehicle ID is required.
- Capacity must be positive.
- Current latitude must be between -90 and 90.
- Current longitude must be between -180 and 180.
- Current address is required.

Duplicate IDs within the same batch request are also rejected.

## Exception Handling

The application uses a centralized exception handling mechanism through `GlobalExceptionHandler`.

It handles:

- Invalid business requests.
- Request validation failures.
- Malformed JSON requests.
- Unexpected server-side exceptions.

This keeps error responses consistent across the REST API.

## Database Operations

Spring Data JPA is used for database interaction.

The application uses batch-oriented service operations:

- Fetch existing records using repository methods.
- Identify existing records using their IDs.
- Update existing records.
- Insert new records.
- Save the resulting collection using `saveAll()`.

Hibernate JDBC batching is configured in `application.yaml` to support more efficient database operations.

## Running Tests

The project includes unit tests using JUnit 5 and Mockito.

Test coverage includes:

- Haversine distance calculation.
- Dispatch allocation logic.
- Vehicle capacity validation.
- Priority-based allocation.
- Handling orders when no vehicles are available.
- Handling empty order lists.
- Handling orders exceeding vehicle capacity.
- Duplicate order ID validation.
- Global exception handling.

Run the complete test suite:

```bash
mvn clean test
```

## Performance Testing

A Python performance testing script is included in the project:

```bash
python3 performance-test.py
```

The script sends a sample workload to the running application:

- 1,000 delivery orders.
- 50 vehicles.
- Multiple order priorities.
- Randomized geographical coordinates.

It measures the time taken for:

- Uploading delivery orders.
- Uploading vehicles.
- Generating the dispatch plan.

It also checks whether assigned orders exceed vehicle capacity.

**Note:** Start the MySQL database and Spring Boot application before executing the performance test.

## Future Improvements

- Implement globally optimized order allocation.
- Integrate real-world road distance APIs.
- Support vehicle availability and scheduling.
- Add authentication and authorization.
- Introduce pagination for large datasets.
- Add integration tests with a dedicated test database.
- Improve route optimization for multiple delivery stops.

## Author

**Utkarsh Sahu**

Java | Spring Boot | Backend Development