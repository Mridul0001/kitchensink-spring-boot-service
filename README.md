# JBoss Migration Demo - Spring Boot Backend

## Important Links

- **[API Documentation](https://app.swaggerhub.com/apis/MRIDULMAHAJAN0001/kitchensink-api/1.0.0)**
- **[Confluence](https://mridulmahajan.atlassian.net/wiki/spaces/~61c40d9b7c6f980070a5416e/pages/294935/Kitchensink+Migration)**

## Project Overview

This is a **Spring Boot** backend for the JBoss Migration Demo project. It provides:
- JWT-based authentication.
- REST APIs to manage member information (create, read, update, delete).
- CORS configuration to allow communication with the React frontend.

## Prerequisites

- Java 21+
- Maven 3.6+
- MongoDB 7.0+

## Project Setup

1. Clone the repository:
    ```bash
    git clone https://github.com/Mridul0001/kitchensink-spring-boot-service.git
    ```

2. Navigate to the project directory:
    ```bash
    cd kitchensink-spring-boot-web-serivce
    ```

3. Build the project:
    ```bash
    mvn clean install
    ```

4. Run the application:
    ```bash
    mvn spring-boot:run -Dspring-boot.run.profiles=<ACTIVE_PROFILE> -Dspring-boot.run.arguments="--mongodb.username=<YOUR_MONGO_USERNAME> --mongodb.password=<YOUR_MONGO_PASSWORD> --security.jwt.secret-key=<JWT_SECRET_KEY_HS256>"

    ```

The server will start at `http://localhost:8080`.

## Application Structure

- **Authentication**: JWT is used for secure authentication.
- **Member Management**: REST APIs to manage members (add, edit, delete, view).
- **MongoDB Integration**: MongoDB is used as the primary database.

## API Endpoints

- **Login API**: 
  - `POST /login`
  - Request Body: `{ "username": "user", "password": "pass" }`
  - Response: `{ "token": "jwt_token" }`

  ***NOTE:** As there is no signup facility, you'll need to create a user with ```ADMIN``` role in ```users``` collection in your MongoDB. User's password should be hashed using ```bcrypt``` hashing. You can use any online tool for this purpose.
        
            {
              "username": "user",
              "password": "<BCRYPT_HASHED_PASSWORD>",
              "role": "ADMIN"
            }
  
- **Member API**:
  - `GET /members` - Retrieve all members.
  - `POST /members` - Add a new member.
  - `PUT /members/{id}` - Update an existing member.
  - `DELETE /members/{id}` - Delete a member.
