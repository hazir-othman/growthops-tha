## Technology Stack
- **Backend:** Java 21, Spring Boot 3.3.1 (Web, Validation, JPA)
- **Cache:** Spring's Native Cache
- **Frontend:** Thymeleaf, HTML5, Bootstrap 5
- **Database:** SQLite (persistent DB file at `data/travel_insurance.db`)
- **Containerization:** Docker & Docker Compose
- **Formatter:** Java: Google Java Format, html: default VS Code formatter

## How to Set Up and Run

### Prerequisites
- Docker & Docker Compose installed, OR
- Java 21 and Maven installed (if running locally).

### Method 1: Containerized Run (Recommended)
We provide a helper shell script to automate everything (directories creation, docker composition, build steps):

1. Run the deploy script:
   ```bash
   ./deploy.sh
   ```
2. Open the application:
   [http://localhost:8081](http://localhost:8081)
3. Check container logs:
   ```bash
   docker compose logs -f
   ```
4. Stop application:
   ```bash
   docker compose down
   ```

### Method 2: Manual Local Build & Run
If you prefer running it locally without Docker:

1. Create a database container folder:
   ```bash
   mkdir -p data
   ```
2. Build and run tests using Maven:
   ```bash
   mvn clean package
   ```
3. Run the Spring Boot application:
   ```bash
   java -jar target/travel-insurance-1.0.0.jar
   ```
4. Access the portal at [http://localhost:8081](http://localhost:8081).

---

## Running Automated Tests
To execute unit and integration tests checking the pricing calculations and NRIC parsing logic:
```bash
mvn test
```
