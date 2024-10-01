# Property Management System

This project implements a simple CRUD (Create, Read, Update, Delete) system for managing real estate properties using a Spring Boot backend and a frontend built with HTML, CSS, and JavaScript. It allows users to create, update, delete, and search for properties, with data stored in a MySQL database.

![Funcionamiento localhost](https://github.com/alexandrac1420/Patrones_Arquitecturales/blob/master/Pictures/localhost.gif)

## Getting Started

These instructions will get you a copy of the project up and running on your local machine and on AWS for development and testing purposes.

### Prerequisites

You need to install the following tools and configure their dependencies:

1. **Java (version 17)**
    ```sh
    java -version
    ```
    Should return something like:
    ```sh
    java version "17.0.7"
    OpenJDK Runtime Environment (build 17.0.7+7-LTS)
    OpenJDK 64-Bit Server VM (build 17.0.7+7-LTS, mixed mode, sharing)
    ```

2. **Maven**
    - Download Maven from [here](http://maven.apache.org/download.html)
    - Follow the installation instructions [here](http://maven.apache.org/download.html#Installation)

    Verify the installation:
    ```sh
    mvn -version
    ```
    Should return something like:
    ```sh
    Apache Maven 3.2.5 (12a6b3acb947671f09b81f49094c53f426d8cea1; 2014-12-14T12:29:23-05:00)
    Maven home: /Users/dnielben/Applications/apache-maven-3.2.5
    Java version: 1.8.0, vendor: Oracle Corporation
    Java home: /Library/Java/JavaVirtualMachines/jdk1.8.0.jdk/Contents/Home/jre
    Default locale: es_ES, platform encoding: UTF-8
    OS name: "mac os x", version: "10.10.1", arch: "x86_64", family: "mac"
    ```

3. **Docker**(For local deployment)
    - Install Docker by following the instructions [here](https://docs.docker.com/get-docker/).
    - Verify the installation:
    ```sh
    docker --version
    ```

4. **Git**
    - Install Git by following the instructions [here](http://git-scm.com/book/en/v2/Getting-Started-Installing-Git)

    Verify the installation:
    ```sh
    git --version
    ```
    Should return something like:
    ```sh
    git version 2.31.1
    ```
---
## How to Use the Property Management System

The Property Management System allows users to manage real estate properties by providing functionalities such as creating new properties, updating existing properties, deleting properties, and searching for properties by address. Additionally, the system supports pagination for easier navigation through the property listings.

### Features:
1. **Add New Properties**: Create new property entries by providing address, price, size, and description.
2. **Update Existing Properties**: Modify the details of a property, such as its price or description.
3. **Delete Properties**: Remove properties that are no longer needed.
4. **Search by Address**: Easily search for properties based on their address.
5. **Pagination**: Navigate through property listings with paginated results.
6. **User Feedback**: The system displays success, error, and validation messages to provide feedback during property management operations.

---
## Running the Project Locally (with Docker)

### Steps to Run Locally:

1. Clone the repository and navigate into the project directory:
    ```sh
    git clone https://github.com/alexandrac1420/Patrones_Arquitecturales.git
    cd Patrones_Arquitecturales
    ```

2. Start the application with Docker Compose:
    ```sh
    docker-compose up -d
    ```

    This will set up both the Spring Boot backend and a MySQL container locally. The backend will be available at `http://localhost:8080`, and the MySQL instance will run inside a Docker container.

3. Build the project using Maven:
    ```sh
    mvn package
    ```

4. Run the application:
    ```sh
    java -jar target/Patrones-0.0.1-SNAPSHOT.jar
    ```

5. Access the application:
    ```sh
    http://localhost:8080
    ```

---

## Running the Project on AWS

In this setup, two **EC2 instances** were used: one for running **MySQL** and another for running the **Spring Boot backend**.

### 1. **Setting Up MySQL on EC2**

1. **Create an EC2 instance** on AWS for the MySQL database, using Amazon Linux 2 as the operating system.

2. **Connect to the EC2 instance**:
    ```sh
    ssh -i your-key.pem ec2-user@<mysql-ec2-instance-ip>
    ```

3. **Install MySQL**:
    ```sh
    sudo yum update -y
    sudo yum install -y https://dev.mysql.com/get/mysql80-community-release-el7-3.noarch.rpm
    sudo yum install -y mysql-community-server
    ```
    ![install](https://github.com/alexandrac1420/Patrones_Arquitecturales/blob/master/Pictures/install.png)

4. **Start MySQL** and enable it to run at boot:
    ```sh
    sudo systemctl start mysqld
    sudo systemctl enable mysqld
    ```

5. **Get the MySQL temporary password**:
    ```sh
    sudo grep 'temporary password' /var/log/mysqld.log
    ```
    ![passwprd](https://github.com/alexandrac1420/Patrones_Arquitecturales/blob/master/Pictures/contrase%C3%B1aTemporal.png)

6. **Secure the MySQL installation**:
    ```sh
    sudo mysql_secure_installation
    ```

7. **Create the MySQL database and user**:
    ```sql
    CREATE DATABASE mydatabase;
    CREATE USER 'myuser'@'%' IDENTIFIED BY 'myP@ssw0rd123!';
    GRANT ALL PRIVILEGES ON mydatabase.* TO 'myuser'@'%';
    FLUSH PRIVILEGES;
    ```

8. **Allow external connections** to MySQL by editing the MySQL config file:
    ```sh
    sudo nano /etc/my.cnf
    ```
    Add the following line under `[mysqld]`:
    ```sh
    bind-address = 0.0.0.0
    ```
    ![cnf](https://github.com/alexandrac1420/Patrones_Arquitecturales/blob/master/Pictures/cnf.png)
   
    Then, restart MySQL:
    ```sh
    sudo systemctl restart mysqld
    ```
9. **Verify MySQL is Running on Port 3306 and Update AWS Security Groups**:
   After setting the `bind-address`, verify that MySQL is running on port `3306` using:
   ```sh
   sudo netstat -tuln | grep 3306
    ```
Ensure port `3306` is open in the AWS Security Group by allowing inbound traffic for MySQL. Similarly, allow port `8080` for the Spring Boot backend to enable external access. This ensures the database and backend services are reachable from outside the EC2 instances.


### 2. **Setting Up Spring Boot Backend on a Different EC2 Instance**

1. **Create another EC2 instance** for the backend and connect to it via SSH:
    ```sh
    ssh -i your-key.pem ec2-user@<backend-ec2-instance-ip>
    ```

2. **Install Java and Maven**:
    ```sh
    sudo yum install java-17-amazon-corretto -y
    sudo yum install maven -y
    ```

3. **Transfer the JAR file** to the EC2 instance using **SFTP**:
   
   First, connect to the EC2 instance using SFTP with your private key:
   ```sh
   sftp -i your-key.pem ec2-user@<backend-ec2-instance-ip>
   ```
   Once connected, use the `put` command to upload the JAR file to the EC2 instance:
    
   ```sh
   put target/Patrones-0.0.1-SNAPSHOT.jar
   ```
   This will upload the JAR file to the EC2 instance. You can confirm the file is in the     instance by listing the directory contents:
    ```sh
   ls
   ```
    After executing `ls`, you should see `Patrones-0.0.1-SNAPSHOT.jar` listed, indicating the file has been successfully transferred.

   

5. **Run the Spring Boot application**:
    ```sh
    java -jar PropertyManagement-0.0.1-SNAPSHOT.jar
    ```

---

## Additional Changes for AWS Deployment

During the deployment to **AWS**, some adjustments were required to make the project work properly:

#### 1. **Modifications to `application.properties`**:

In the **Spring Boot backend**, the `application.properties` file needed to be updated to connect to the **MySQL EC2 instance** instead of a local database. The following changes were made:

```properties
spring.datasource.url=jdbc:mysql://<mysql-ec2-instance-ip>:3306/property_management
spring.datasource.username=myuser
spring.datasource.password=mypassword
```
This configuration allows the Spring Boot application to connect to the remote MySQL database hosted on the other EC2 instance.

### 2. CORS Configuration in `PropertyController`:
To enable the frontend, hosted on a different origin (such as localhost or AWS EC2), to communicate with the backend, **Cross-Origin Resource Sharing (CORS)** needed to be enabled in the controller:

```java
@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/properties")
public class PropertyController {
    // Controller code
}
```
This allows requests from different origins (e.g., the frontend running on a different machine) to access the backend. Without enabling CORS, requests from different origins would be blocked by the browser, resulting in CORS policy errors.

### 3. Modifications to `index.html`:
The API endpoint in `index.html` also needed to be updated to point to the public IP address of the **EC2 instance** running the backend instead of `localhost`. The updated `apiBaseUrl` in the frontend would look like this:

```javascript
const apiBaseUrl = 'http://<backend-ec2-instance-ip>:8080/properties';
```
This ensures that the frontend communicates with the backend hosted on AWS EC2, rather than a locally hosted backend.

    
## Architecture
![Architecture Diagram](https://github.com/alexandrac1420/Patrones_Arquitecturales/blob/master/Pictures/Arquitectura.png)
### Overview

The Property Management System is designed to manage properties using a typical client-server architecture. The frontend is a web interface that communicates with the Spring Boot backend via RESTful APIs. The backend interacts with a MySQL database to store property data. The system supports CRUD operations for property management, and it has been deployed on AWS, using separate EC2 instances for the backend and the database.

### Components

#### 1. **Frontend (HTML/JavaScript/CSS)**
   - **Role**: Provides a user interface for property management, allowing users to interact with the backend API through forms and tables.
   - **Responsibilities**:
     - Display property information in a table format.
     - Provide forms for creating and updating property data.
     - Allow users to search properties by address.
     - Display validation messages and errors to the user.
   
#### 2. **Spring Boot Backend**
   - **Role**: Serves as the backend API for the frontend, handling business logic and database operations.
   - **Responsibilities**:
     - Provide RESTful APIs for CRUD operations (Create, Read, Update, Delete) on properties.
     - Handle requests from the frontend, process data, and send appropriate responses.
     - Interact with the MySQL database using JPA for data persistence.
     - Implement pagination and search functionality for property listings.

#### 3. **MySQL Database**
   - **Role**: Stores property information, including address, price, size, and description.
   - **Responsibilities**:
     - Ensure data persistence for property records.
     - Handle database queries efficiently.
     - Maintain the integrity and structure of the property data.

### Interaction Flow

1. **Frontend Interaction**: Users access the web interface, where they can manage properties (create, update, search, delete).
2. **API Requests**: The frontend sends HTTP requests (GET, POST, PUT, DELETE) to the Spring Boot backend.
3. **Data Processing**: The backend processes the requests, applying business logic, and interacts with the MySQL database to retrieve or modify data.
4. **Response Handling**: The backend returns JSON responses to the frontend, which updates the user interface accordingly.
5. **Pagination and Search**: The backend supports pagination and searching by address, returning paginated results for the frontend.

## Class Diagram

![Class Diagram](https://github.com/alexandrac1420/Patrones_Arquitecturales/blob/master/Pictures/DiagramaClases.png)

### Overview

The class diagram provides a detailed view of the components in the backend, including the controller, service, and repository layers. The main classes involved are `PropertyController`, `PropertyService`, and `PropertyRepository`, each of which plays a distinct role in the application.

### Class Descriptions

#### 1. **PropertyController**
   - **Role**: Acts as the entry point for HTTP requests from the frontend.
   - **Key Responsibilities**:
     - Handle incoming API requests (GET, POST, PUT, DELETE).
     - Delegate business logic to the `PropertyService`.
     - Return responses in JSON format to the client.
     - Handle pagination and search functionality for properties.

#### 2. **PropertyService**
   - **Role**: Contains the business logic for managing properties.
   - **Key Responsibilities**:
     - Create, update, retrieve, and delete properties.
     - Communicate with `PropertyRepository` to interact with the MySQL database.
     - Validate data before performing database operations.
     - Implement pagination and search features for efficient data retrieval.

#### 3. **PropertyRepository**
   - **Role**: Interface that provides access to the MySQL database using JPA.
   - **Key Responsibilities**:
     - Perform CRUD operations on the `Property` entity.
     - Handle database queries and transactions.

#### 4. **Property**
   - **Role**: Entity class that represents a property.
   - **Key Responsibilities**:
     - Define the structure of the property data (e.g., ID, address, price, size, description).
     - Map the class to the `properties` table in the MySQL database using JPA annotations.

### Relationships Between Classes

- **`PropertyController` and `PropertyService`**:
  - `PropertyController` receives HTTP requests and delegates the business logic to `PropertyService`.
  
- **`PropertyService` and `PropertyRepository`**:
  - `PropertyService` interacts with `PropertyRepository` to perform database operations such as saving, updating, and deleting property records.
  
- **`PropertyRepository` and `Property`**:
  - `PropertyRepository` is responsible for CRUD operations on the `Property` entity, mapping it to the database.

## Test Report

### Author
Name: Alexandra Cortes Tovar

### Date
Date: 03/10/2024

### Summary
This report outlines the unit and integration tests conducted for the Property Management System project. The tests focus on validating CRUD operations for the `Property` entity, ensuring that the system behaves correctly when creating, updating, deleting, and retrieving properties.

### Tests Conducted

#### Controller Tests

1. **Test `testCreateProperty`**
   - **Description**: Validates that a property can be created and stored in the database.
   - **Objective**: Ensure the backend API can create a new property and persist it in the database.
   - **Testing Scenario**: Simulate a POST request to create a property with valid data.
   - **Expected Behavior**: The property is successfully created and returned with a unique ID.
   - **Verification**: Confirms that the response contains the correct property data and that the property is saved in the database.

2. **Test `testGetAllProperties`**
   - **Description**: Ensures that all properties can be retrieved with pagination.
   - **Objective**: Validate that the API returns a paginated list of properties.
   - **Testing Scenario**: Simulate a GET request to retrieve all properties with pagination.
   - **Expected Behavior**: The API returns a list of properties, along with pagination details (page number, size, and total pages).
   - **Verification**: Confirms that the response contains the correct number of properties and pagination data.

3. **Test `testUpdateProperty`**
   - **Description**: Tests the ability to update existing property details.
   - **Objective**: Ensure that the backend can update a property's information and persist the changes in the database.
   - **Testing Scenario**: Simulate a PUT request to update a property's details (e.g., address, price).
   - **Expected Behavior**: The property is successfully updated, and the changes are reflected in the database.
   - **Verification**: Confirms that the updated property is returned with the modified information, ensuring that the property data is correctly updated in the database.

4. **Test `testDeleteProperty`**
   - **Description**: Tests the deletion of a property from the system.
   - **Objective**: Ensure that the backend can delete a property and remove it from the database.
   - **Testing Scenario**: Simulate a DELETE request to remove a property by ID.
   - **Expected Behavior**: The property is successfully deleted, and the deletion is confirmed.
   - **Verification**: Confirms that the property is no longer present in the database.

5. **Test `testSearchPropertiesByAddress`**
   - **Description**: Validates that properties can be searched by their address.
   - **Objective**: Ensure that the search functionality returns the correct properties based on the address query.
   - **Testing Scenario**: Simulate a GET request to search for properties by address (e.g., "Calle 1").
   - **Expected Behavior**: The API returns a list of properties that match the search query, with pagination applied.
   - **Verification**: Confirms that the response contains properties with addresses that match the search term and that pagination works as expected.

---

#### Service Layer Tests

1. **Test `testGetAllProperties`**
   - **Description**: Tests the retrieval of all properties with pagination in the service layer.
   - **Objective**: Ensure that the `PropertyService` can return paginated property data from the database.
   - **Testing Scenario**: Simulate a call to the `getAllProperties` method in `PropertyService`.
   - **Expected Behavior**: The method returns a paginated list of properties.
   - **Verification**: Confirms that the correct page of properties is returned from the repository and that pagination details are handled properly.

2. **Test `testGetPropertyById`**
   - **Description**: Validates that a specific property can be retrieved by its ID in the service layer.
   - **Objective**: Ensure that the `PropertyService` returns the correct property based on the provided ID.
   - **Testing Scenario**: Simulate a call to the `getPropertyById` method.
   - **Expected Behavior**: The method returns the property details associated with the given ID.
   - **Verification**: Confirms that the returned property has the correct ID and associated details, verifying proper query handling.

3. **Test `testCreateProperty`**
   - **Description**: Tests the property creation logic in the service layer.
   - **Objective**: Ensure that the `PropertyService` can create and save a new property in the database.
   - **Testing Scenario**: Simulate a call to the `createProperty` method.
   - **Expected Behavior**: The property is successfully saved to the database.
   - **Verification**: Confirms that the `save` method in `PropertyRepository` is called and the property data is persisted correctly.

4. **Test `testUpdateProperty`**
   - **Description**: Tests the property update logic in the service layer.
   - **Objective**: Ensure that the `PropertyService` can update an existing property in the database.
   - **Testing Scenario**: Simulate a call to the `updateProperty` method in `PropertyService`.
   - **Expected Behavior**: The property is successfully updated in the database.
   - **Verification**: Confirms that the updated property is saved correctly and the repository methods are called as expected.

5. **Test `testDeleteProperty`**
   - **Description**: Tests the deletion logic for properties in the service layer.
   - **Objective**: Ensure that the `PropertyService` can delete a property from the database.
   - **Testing Scenario**: Simulate a call to the `deleteProperty` method in `PropertyService`.
   - **Expected Behavior**: The property is deleted from the database.
   - **Verification**: Confirms that the repository’s `delete` method is called and the property is successfully removed from the database.

![Test report](https://github.com/alexandrac1420/Patrones_Arquitecturales/blob/master/Pictures/test.png)

## Docker Compose Configuration

The `docker-compose.yml` file contains the configuration needed to run both the MySQL database and the Spring Boot backend in Docker containers. Below is an explanation of the key parts of the configuration:

```yaml
services:
  mysql:
    container_name: 'propierties-mysql'
    image: 'mysql:latest'
    environment:
      - 'MYSQL_DATABASE=propierties'
      - 'MYSQL_PASSWORD=secret'
      - 'MYSQL_ROOT_PASSWORD=verysecret'
      - 'MYSQL_USER=myuser'
    ports:
      - '3306:3306'
```
### Explanation:

#### Services:
The `services` section defines the containers that will be started by Docker Compose. In this case, it includes the `mysql` container for the MySQL database.

#### mysql:
- **container_name**: The name given to the MySQL container. In this case, the container is named `propierties-mysql`.

- **image**: Specifies the Docker image to use for the MySQL service. Here, the latest version of the official `mysql` image is used.

- **environment**: Defines environment variables that configure MySQL:
  - `MYSQL_DATABASE`: The name of the database to be created inside the MySQL container.
  - `MYSQL_PASSWORD`: The password for the `myuser` account.
  - `MYSQL_ROOT_PASSWORD`: The root password for the MySQL server.
  - `MYSQL_USER`: The name of the non-root user who will have access to the database.

- **ports**: Maps port `3306` of the MySQL container to port `3306` of the host machine. This allows the MySQL database to be accessed from outside the container, for example, by the Spring Boot backend or external database clients.

This configuration allows you to run a MySQL database containerized within Docker, along with your Spring Boot backend, using Docker Compose for a simplified local development environment.

## Built With

* [Maven](https://maven.apache.org/) - Dependency Management
* [Spring Boot](https://spring.io/projects/spring-boot) - Backend Framework
* [MySQL](https://www.mysql.com/) - Database
* [Docker](https://www.docker.com/) - Containerization (for local development)
* [Git](http://git-scm.com/) - Version Control System

## Versioning

I use [GitHub](https://github.com/) for versioning. For the versions available, see the [tags on this repository](https://github.com/alexandrac1420/Patrones_Arquitecturales.git).

## Authors

* **Alexandra Cortes Tovar** - [alexandrac1420](https://github.com/alexandrac1420)

## License

This project is licensed under the GNU
