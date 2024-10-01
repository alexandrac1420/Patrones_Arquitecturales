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

### Installing Locally with Docker Compose

1. Clone the repository and navigate into the project directory:
    ```sh
    git clone https://github.com/alexandrac1420/Patrones_Arquitecturales.git
    cd Patrones_Arquitecturales
    ```

2. Start the application with Docker Compose:
    ```sh
    docker-compose up -d
    ```

    This will start both the Spring Boot backend and a MySQL container. The backend will be available on `http://localhost:8080`, and MySQL will be running inside the Docker container.

3. Build the project:
    ```sh
    mvn package
    ```

    Should display output similar to:
    ```sh
    [INFO] --- jar:3.3.0:jar (default-jar) @ AplicacionesDistriuidas ---
    [INFO] Building jar: C:\Users\alexa\OneDrive\Escritorio\Aplicaciones_Distribuidas\target\Patrones-0.0.1-SNAPSHOT.jar
    [INFO] BUILD SUCCESS
    ```

4. Run the application:
    ```sh
    java -jar target/Patrones-0.0.1-SNAPSHOT.jar
    ```

4. Verify that the application is running by visiting:
    ```sh
    http://localhost:8080
    ```

### Running in AWS

![Funcionamiento AWS](https://github.com/alexandrac1420/Patrones_Arquitecturales/blob/master/Pictures/aws.gif)


#### 1. Install MySQL in an EC2 Instance

Follow these steps to install MySQL on one EC2 instance:

1. **Connect to your EC2 instance** (using SSH):
    ```sh
    ssh -i your-key.pem ec2-user@<ec2-mysql-instance-public-ip>
    ```

2. **Install MySQL**:
    ```sh
    sudo yum update -y
    sudo yum install -y https://dev.mysql.com/get/mysql80-community-release-el7-3.noarch.rpm
    sudo yum install -y mysql-community-server
    ```

3. **Start MySQL**:
    ```sh
    sudo systemctl start mysqld
    sudo systemctl enable mysqld
    ```

4. **Get the MySQL temporary password**:
    ```sh
    sudo grep 'temporary password' /var/log/mysqld.log
    ```

5. **Secure MySQL Installation**:
    ```sh
    sudo mysql_secure_installation
    ```

    Follow the prompts to set a new password, remove anonymous users, disallow remote root login, and remove test databases.

6. **Create a database and user**:
    ```sh
    mysql -u root -p
    ```

    Inside the MySQL prompt:
    ```sql
    CREATE DATABASE property_management;
    CREATE USER 'myuser'@'%' IDENTIFIED BY 'mypassword';
    GRANT ALL PRIVILEGES ON property_management.* TO 'myuser'@'%';
    FLUSH PRIVILEGES;
    EXIT;
    ```

7. **Allow external connections**:
    - Edit the MySQL configuration file to allow remote connections:
      ```sh
      sudo nano /etc/my.cnf
      ```
      Add or modify the following line under `[mysqld]`:
      ```sh
      bind-address = 0.0.0.0
      ```

    - Restart MySQL:
      ```sh
      sudo systemctl restart mysqld
      ```

8. **Open MySQL port (3306)** in the EC2 security group to allow external access.

#### 2. Set Up Spring Boot Backend in Another EC2 Instance

1. **Connect to your EC2 instance** (using SSH):
    ```sh
    ssh -i your-key.pem ec2-user@<ec2-spring-boot-instance-public-ip>
    ```

2. **Install Java and Maven**:
    ```sh
    sudo yum install java-17-amazon-corretto -y
    sudo yum install maven -y
    ```

3. **Copy the JAR file to the EC2 instance** (from your local machine):
    ```sh
    scp -i your-key.pem target/PropertyManagement-0.0.1-SNAPSHOT.jar ec2-user@<ec2-spring-boot-instance-public-ip>:~
    ```

4. **Run the Spring Boot application**:
    ```sh
    java -jar PropertyManagement-0.0.1-SNAPSHOT.jar
    ```

#### 3. Update Spring Boot Configuration to Connect to MySQL in AWS

1. Modify `src/main/resources/application.properties` to point to the MySQL instance in AWS:
    ```properties
    spring.datasource.url=jdbc:mysql://<mysql-ec2-instance-public-ip>:3306/property_management
    spring.datasource.username=myuser
    spring.datasource.password=mypassword
    ```

2. Restart the Spring Boot application to apply the changes.

3. **Open port 8080** in the EC2 security group of the Spring Boot instance to allow access to the application.

    
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

[!Test report](https://github.com/alexandrac1420/Patrones_Arquitecturales/blob/master/Pictures/test.png)


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
