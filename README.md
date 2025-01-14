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
• The client includes the token in the Authorization header of HTTP requests: