# Library Management System

A backend-focused Library Management System developed using Java and Spring Boot. The project aims to simplify library operations, including student management, book management, book transactions, fine calculation, and online fine payments.

**Project Status:** 🚧 Actively Under Development

## Tech Stack

- Java 21
- Spring Boot
- Spring Data JPA
- Hibernate
- MySQL
- Maven
- REST APIs
- Postman
- Cashfree Payment Gateway (Sandbox)

## Implemented Features

- Student Management REST APIs
- Book Management REST APIs
- MySQL database integration
- Entity, Repository, Service, and Controller architecture
- Basic CRUD operations
- Cashfree Payment Gateway Sandbox Integration
- UPI payment testing in Cashfree Sandbox for library fine payments

## In Progress

- Librarian Management
- Book Issue and Return functionality
- Book Transaction Management
- Fine Calculation and Management
- Payment verification and webhook handling
- API validation and security improvements

## Project Architecture

The application follows a layered architecture:

- **Controller:** Handles HTTP requests and responses.
- **Service:** Manages business logic.
- **Repository:** Handles database operations.
- **Model:** Defines application entities.

## Payment Integration

Cashfree Payment Gateway is integrated in Sandbox mode to explore online library fine payments.

- Payment order integration
- UPI payment testing in Sandbox
- Payment integration with the Spring Boot backend

**Note:** This project currently uses Cashfree Sandbox credentials. Live payment processing has not been deployed.

## Getting Started

1. Clone this repository.
2. Configure your MySQL database.
3. Configure database credentials using environment variables.
4. Add Cashfree Sandbox credentials.
5. Run the application using Maven.
6. Test the REST APIs using Postman.

## Development Updates

This is an ongoing personal project. New features, improvements, and bug fixes are added regularly.

## Author

**Tejas Raj**

- GitHub: https://github.com/Tejasrajemax
- LinkedIn: https://www.linkedin.com/in/tejas-raj-631322297/
