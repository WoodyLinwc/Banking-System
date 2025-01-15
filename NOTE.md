# Banking-System


mkdir -p banking-system/src/main/java/com/bankapp/{config,controller,model,dto,repository,service,security,exception,util}



psql -U postgres
CREATE DATABASE bankapp;
- list of database
\l
- connect to database
\c bankapp
- list of tables
\dt
- display mode
\x




DAO
DTO

JWT vs Password
JWTs are widely used in modern authentication protocols like OAuth 2.0 and OpenID Connect.
How JWT(JSON Web Token) works:
1. Authentication:
• A user logs in and provides valid credentials.
• The server verifies the credentials, generates a JWT, and sends it to the user.
2. Client-Side Storage:
• The client stores the token (e.g., in localStorage or cookies).
3. Subsequent Requests:
• The client includes the token in the Authorization header of HTTP requests

No need to store session state on the server, making the system stateless and scalable.
Passwords prove who you are (authentication), but JWTs can also handle what you can do (authorization).

OpenAPI: Think of it as the "what" — the specification that defines how APIs should be described.
Swagger: Think of it as the "how" — the tools and ecosystem that help you work with OpenAPI.
If you're starting with API design and documentation today, you'll primarily use OpenAPI as the standard, likely supported by Swagger tools.


Service Layer & User Management:

User registration and login
JWT authentication
Password encryption
Basic user CRUD operations


Account Management:

Account creation
Account listing and details
Account status management
Balance updates


Transaction Management:

Deposit functionality
Withdrawal functionality
Transfer between accounts
Transaction history


Admin Features:

Admin dashboard
User management for admins
System reports
Account oversight


Testing:

Unit tests
Integration tests
Security tests

SwaggerAPI/OpenAPI


Backend test Flow 

Frontend Development:

React components for user interface
Authentication flows
Account management interface
Transaction interface

The flow to test would be:

Create an account (if you haven't already)
Make a deposit
Check your balance
Make a withdrawal
View transaction history


Make a comprehensive README for this project
How can other people use this project, like fork this project and test all API themselves
