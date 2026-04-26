# PropPortal - Real Estate Management System
## SE1020 Object Oriented Programming Project

### Prerequisites
- Java 17+
- Maven 3.8+
- MySQL 8.0+
- IntelliJ IDEA (recommended)

### Setup Instructions

#### 1. Configure MySQL
Open MySQL and run:
```sql
CREATE DATABASE real_estate_portal;
```

#### 2. Update Database Credentials
Edit `src/main/resources/application.properties`:
```properties
spring.datasource.username=root
spring.datasource.password=YOUR_MYSQL_PASSWORD
```

#### 3. Run the Application
```bash
mvn spring-boot:run
```
Or open in IntelliJ IDEA and run `RealEstatePortalApplication.java`

#### 4. Access the App
Open browser: http://localhost:8080

### Default Admin Login
- Email: `admin@realestate.com`
- Password: `admin123`

### OOP Concepts Implemented

| Concept | Where Applied |
|---------|---------------|
| **Encapsulation** | All model classes use private fields with getters/setters |
| **Inheritance** | SystemUser → Buyer, Renter, Admin / Property → Residential, Commercial / Lister → Agent, IndependentSeller / Feedback → PropertyQuestion, AgentReview |
| **Polymorphism** | getUserType(), getProfileSummary(), getDashboardRedirect(), calculateTax(), getDisplayDetails(), getFeedbackType() |
| **Abstraction** | Abstract base classes with abstract methods |

### Project Structure
```
src/main/java/com/realestate/
├── model/          # 10 OOP model classes (3 inheritance hierarchies)
├── repository/     # 12 Spring Data JPA repositories
├── service/        # 5 service classes
└── controller/     # 6 controllers (one per component)
```
