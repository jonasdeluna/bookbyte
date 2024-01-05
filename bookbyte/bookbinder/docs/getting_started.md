# Startup Documentation for "Bookbinder" - Spring Boot REST Server

## Introduction

"Bookbinder" is a Spring Boot REST server designed for managing a comprehensive library system. It handles
functionalities for managing books, library books, and personal records. The server operates on `localhost:8080`, and
adheres to the REST principles.

## Getting Started

### Dependencies

- Java JDK 17.0.8.1
- Maven 3.9.4
- Spring Boot 3.1.5

### Starting the Server

To start the "Bookbinder" server, follow these steps:

1. **Navigate to the Project Directory:**

   Open your terminal and navigate to the `bookbyte/bookbinder` directory: `cd path/to/bookbyte/bookbinder`
2. Start the server: `./mvnw spring-boot:run`

The server will start, and you should see log messages indicating that it's running on port 8080.

### Accessing the API

Once the server is up and running, you can access the REST API at http://localhost:8080. The API provides a Swagger UI
documentation, which makes it easy to explore and interact with the API endpoints. The full api docs can be
found [here (rest.html)](./rest.html)

### API Overview

The Bookbinder API offers a range of endpoints for managing books, library books, and persons. Key operations include:

* Creating, updating, fetching, and deleting books.
* Managing library books, including adding, updating, fetching details, borrowing, and returning.
* Adding, updating, and retrieving personal records.

For detailed information on each endpoint, refer to the Swagger documentation available through the API.