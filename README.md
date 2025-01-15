# Banking System Backend

A robust banking system built with Spring Boot that provides secure account management, transactions, and administrative features.

## Technologies Used

### Core Dependencies
- **Spring Boot** (3.2.2)
  - Spring Boot Starter Web
  - Spring Boot Starter Data JPA
  - Spring Boot Starter Security
  - Spring Boot Starter Validation
  - Spring Boot Starter Mail
  - Spring Boot Starter Thymeleaf
- **Database**
  - PostgreSQL
  - Spring Data JPA
- **Security**
  - Spring Security
  - JWT (JSON Web Token) 0.12.3
- **Documentation**
  - SpringDoc OpenAPI (Swagger) 2.3.0
- **Testing**
  - JUnit 5
  - Mockito
  - H2 Database (for testing)
- **Other**
  - Jakarta Validation
  - Java 17
  - Maven

## Project Structure
```
src/
├── main/
│   ├── java/
│   │   └── com/
│   │       └── bankapp/
│   │           ├── config/          # Configuration classes
│   │           ├── controller/      # REST endpoints
│   │           ├── dto/            # Data Transfer Objects
│   │           ├── model/          # Entity classes
│   │           ├── repository/     # Data access layer
│   │           ├── security/       # Security configuration
│   │           ├── service/        # Business logic
│   │           └── util/           # Utility classes
│   └── resources/
│       ├── application.properties  # Application configuration
│       └── templates/             # Email templates
└── test/
    └── java/
        └── com/
            └── bankapp/
                └── service/        # Service tests
```

## Features

### User Management
- User registration and login
- JWT-based authentication
- Role-based authorization (USER, ADMIN)
- Profile management

### Account Management
- Create different types of accounts (Savings, Checking)
- View account details and balance
- Account status management

### Transaction Management
- Deposit funds
- Withdraw funds
- Transfer between accounts
- Transaction history
- Email notifications for transactions

### Admin Features
- User management
- Account oversight
- System statistics
- Transaction monitoring

## Setup and Installation

### Prerequisites
- Java 17 or higher
- Maven 3.6 or higher
- PostgreSQL 12 or higher

### Database Setup
```sql
CREATE DATABASE bankapp;
```

### Configuration
Update `src/main/resources/application.properties`:
```properties
# Database Configuration
spring.datasource.url=jdbc:postgresql://localhost:5432/bankapp
spring.datasource.username=your_username
spring.datasource.password=your_password

# JWT Configuration
jwt.secret=your_jwt_secret_key
jwt.expiration=86400000

# Email Configuration (if using email features)
spring.mail.host=smtp.gmail.com
spring.mail.port=587
spring.mail.username=your_email@gmail.com
spring.mail.password=your_app_specific_password
```

### Building and Running
```bash
# Build the project
mvn clean install

# Run the application
mvn spring-boot:run
```

## API Documentation

### Authentication Endpoints
```http
# Register a new user
POST /api/auth/register
Content-Type: application/json

{
    "firstName": "John",
    "lastName": "Doe",
    "email": "john@example.com",
    "password": "password123"
}

# Login
POST /api/auth/login
Content-Type: application/json

{
    "email": "john@example.com",
    "password": "password123"
}
```

### Account Endpoints
```http
# Create account
POST /api/accounts
Authorization: Bearer {jwt_token}
Content-Type: application/json

{
    "type": "SAVINGS"
}

# Get user accounts
GET /api/accounts
Authorization: Bearer {jwt_token}
```

### Transaction Endpoints
```http
# Deposit
POST /api/accounts/{accountNumber}/transactions/deposit
Authorization: Bearer {jwt_token}
Content-Type: application/json

{
    "amount": 1000.00,
    "description": "Initial deposit"
}

# Transfer
POST /api/accounts/{accountNumber}/transactions/transfer
Authorization: Bearer {jwt_token}
Content-Type: application/json

{
    "amount": 100.00,
    "description": "Transfer to savings",
    "destinationAccountNumber": "destination_account_number"
}
```

### Admin Endpoints
```http
# Get dashboard statistics
GET /api/admin/dashboard
Authorization: Bearer {jwt_token}

# Get all users
GET /api/admin/users
Authorization: Bearer {jwt_token}
```

## Testing

### Running Tests
```bash
# Run all tests
mvn test

# Run specific test class
mvn test -Dtest=UserServiceTest

# Run with coverage report
mvn clean test
```

### API Testing with Postman
1. Import the Postman collection from `postman/Banking_System_API.json`
2. Set up environment variables:
   - `baseUrl`: http://localhost:8080
   - `jwt_token`: Token received from login

## Security Features
- Password encryption using BCrypt
- JWT-based authentication
- Role-based access control
- Secure email notifications
- Input validation
- Transaction validation

## Error Handling
- Global exception handling
- Custom exceptions for business logic
- Validation error responses
- Proper HTTP status codes

## Best Practices
- DTOs for data transfer
- Service layer for business logic
- Repository pattern for data access
- Unit testing with Mockito
- Swagger documentation
- Proper error handling
- Transaction management
- Secure password handling

## Contributing
1. Fork the repository
2. Create a feature branch
3. Commit your changes
4. Push to the branch
5. Create a Pull Request

## License
This project is licensed under the MIT License - see the LICENSE file for details.
