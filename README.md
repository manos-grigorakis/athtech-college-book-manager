# Book Manager
CCS6213 - Backend Application Development | MSc in Web & Mobile Development 2025-2026

## Prerequisites

- Java 21
- Maven 3
- Docker & Docker Compose

## Setup

1. Clone the repo
   ```bash
   git clone https://github.com/manos-grigorakis/athtech-college-book-manager.git
   ```
2. Copy environment variables

   ```bash
   cp .env.example .env
   ```

3. Start database via Docker Compose
   ```bash
   docker compose up -d
   ```
4. Populate the database with sample user
   > Password: admin

   - Option A: Run the SQL file from a database client \
        The query exists in: `/src/main/resources/db/setup.sql` \
        Verify that the user is created in the database after executing the query.

   - Option B: Docker command
      1. Create the User
         ```bash
         docker exec -e PGPASSWORD=myPassword -i bookmanager-db-1 \
         psql -U book_manager_user -d book_manager \
         -c "INSERT INTO users(name, email, password, created_at)
         VALUES ('John Doe', 'john@example.com', '\$2a\$10\$dVLMaN3yEmbXBY1yxhC6WOVZkevCOGbX9mODq.wSVMrgYSgyQaXuW', NOW());"
         ```
         
     2. Verify
        ```bash
        docker exec -e PGPASSWORD=myPassword -i bookmanager-db-1 \
        psql -U book_manager_user -d book_manager \
        -c "SELECT email FROM users;"
        ```
    
5. Start backend server
   1. With Maven
      ```bash
      mvn spring-boot:run
      ```
   2. Without Maven
      ```bash
      ./mvnw spring-boot:run
      ```
   3. With IntelliJ IDEA \
       Open the project with IntelliJ IDEA and the run configuration will automatically load
