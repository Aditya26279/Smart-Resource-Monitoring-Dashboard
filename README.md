# Smart Resource Monitoring Dashboard

A comprehensive system metrics monitoring solution developed by integrating core computer science concepts: Computer Networks, Operating Systems, Data Mining & Warehousing, Object-Oriented Analysis and Design, and Advanced Java Programming.

## Features

-   **Deep OS Integration**: Direct polling of system kernel metrics (CPU, RAM, Disk I/O) using the Operating System and Hardware Information (OSHI) bindings.
-   **Real-Time Data Streaming**: Active WebSocket pipelines enabling instant bidirectional communication (Publish/Subscribe) between backend and frontend.
-   **Data Warehousing**: Automated aggregation of system history stored in an intuitive relational database scheme via Spring Data JPA.
-   **Historical Analytics**: On-demand AI/Data Mining analysis to retrieve system outliers and usage threshold anomalies.
-   **Glassmorphism Dashboard**: A futuristic, extremely responsive front-end dashboard powered by raw HTML/CSS and real-time Chart.js.

## Technologies Used

*   **Backend**: Java 17, Spring Boot, Spring WebSockets, Spring Data JPA
*   **Hardware Interface**: OSHI
*   **Database**: H2 (In-Memory Database for demonstration)
*   **Frontend**: HTML5, Modern CSS (CSS Variables, Flexbox/Grid), JavaScript (ES6)
*   **Libraries**: STOMP.js, SockJS, Chart.js

## Project Architeture (OOAD)

The system leverages Strategy design patterns (`MetricCollector` interface and implementations) and Singleton services to ensure scalability, ease of maintenance, and solid design foundations.

## Prerequisites

Before running this application, please ensure you have the following installed:
*   **Java 17 (or higher)**
*   **Maven** 
*   **A Modern Web Browser** (Chrome, Firefox, Safari)

## How to Run the Application

### Option 1: Using the provided batch script (Windows)

1. Double click the `run.bat` file in the root directory.
2. The script will automatically build the project with Maven and launch the application.
3. Open a browser and navigate to `http://localhost:8080`.

### Option 2: Using Maven

1. Open your terminal or command prompt.
2. Navigate to the project root directory.
3. Build the project using Maven:
   ```bash
   mvn clean package
   ```
4. Run the generated JAR file:
   ```bash
   java -jar target/dashboard-0.0.1-SNAPSHOT.jar
   ```
5. Alternatively, run using the Spring Boot plugin:
   ```bash
   mvn spring-boot:run
   ```
6. Open your web browser and navigate to `http://localhost:8080`.

## Architecture Diagram Overview

```text
[OS Hardware / Kernel]
       ^
       | (OSHI)
       v
+--------------------------+
| Spring Boot Backend      |
|  - CpuCollector          |
|  - MemoryCollector       |
|  - DiskCollector         |
|                          |
|  - DataMiningService     | <-- [H2 Database / JPA]
+--------------------------+
       ^
       | (WebSockets & REST API)
       v
[Web Dashboard Client]
  - Real-time Charts (Chart.js)
  - Analytics Notifications
```
