# **E-commerce API**

This is an API for managing the e-commerce platform, including user authentication, product management, order processing, and reports.

---

## **Technologies Used**

- **Spring Boot**: Framework for building the RESTful API.
- **MySQL 8.0**: Relational user and analytics information.
- **Kafka 3.8.1**: For event streaming and messaging.
- **Elasticsearch 8.0**: Database for storing products and orders.
- **Java 17**: The programming language used for backend development.

---

## **Features**

- **Authentication & Registration**: Secure login and registration endpoints.
- **Product Management**: Admin can create, update, delete, and search products.
- **Order Management**: Users can create orders and process payments.
- **Reports**: Admin can access various reports like top customers, average ticket size, and monthly revenue.
- **Role-based Access Control**: Different access for admins and users.

---

## **API Endpoints**

### **Authentication & Registration**
- **POST /auth/login**: User login (Public)
- **POST /auth/register**: User registration (Public)

### **Admin Routes**
Admin routes are restricted to users with the **ADMIN** role. These routes allow full management of products and reports:
- **POST /products**: Create a new product.
- **PUT /products/{id}**: Update the details of an existing product.
- **PATCH /products/{id}**: Partially update product details.
- **DELETE /products/{id}**: Delete a product.
- **GET /reports/top-customers**: Get the top customers report.
- **GET /reports/average-ticket**: Get the average ticket size report.
- **GET /reports/monthly-revenue**: Get the monthly revenue report.

### **User Routes**
User routes are available to both **USER** and **ADMIN** roles. These routes allow users to place orders and make payments:
- **POST /orders**: Create a new order.
- **POST /orders/{id}/payment**: Process payment for an order.

### **Search Routes**
Search routes are open to both **USER** and **ADMIN** roles and allow searching for products:
- **GET /products/search**: Search for products.


---


## **Setup Instructions**

### **Prerequisites**

- **JDK 17**
- **MySQL 8.0**
- **Kafka 3.8.1**
- **Elasticsearch 8.0**

### **Running Locally**

1. Clone the repository:

```bash
git clone https://github.com/yourusername/ecommerce-api.git
cd ecommerce-api

1. Set up your MySQL database, Kafka, and Elasticsearch instances.

2. Configure the database application properties in `src/main/resources/application.properties`:

```properties
spring.datasource.username=yourusername
spring.datasource.password=yourpassword
spring.elasticsearch.username=elastic
spring.elasticsearch.password=password
```

3. There is a `schema.sql` file in the project that can be used to automatically create the necessary database structure in MySQL. Additionally, the `AdminInitializer` class automatically creates an admin user in the database, and the `ElasticsearchInitializer` class creates the indexes in Elasticsearch if they do not already exist. Both of these classes are executed automatically upon application startup.

3. Run the application:

```bash
mvn spring-boot:run
```

4. Access the API at `http://localhost:8080`.