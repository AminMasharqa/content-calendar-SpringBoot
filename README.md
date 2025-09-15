# Content Calendar Backend API 📅

A Spring Boot REST API for managing content creation and publishing workflow. This application helps content creators track their ideas, articles, videos, courses, and conference talks through different stages of development.

## 🚀 Features

- **Content Management**: Create, read, update, and delete content items
- **Status Tracking**: Track content through different stages (IDEA → IN_PROGRESS → COMPLETED → PUBLISHED)
- **Content Types**: Support for Articles, Videos, Courses, and Conference Talks
- **Search & Filter**: Find content by title keywords or status
- **Database Persistence**: PostgreSQL database with automatic schema initialization
- **Data Loading**: Automatic loading of sample data from JSON file
- **RESTful API**: Clean REST endpoints with proper HTTP status codes
- **Configuration Management**: Externalized configuration with environment variables

## 🛠️ Tech Stack

- **Java 21** - Latest LTS version
- **Spring Boot 3.5.5** - Framework
- **Spring Data JDBC** - Data persistence
- **PostgreSQL** - Primary database
- **Maven** - Build tool
- **Jackson** - JSON processing
- **Bean Validation** - Input validation
- **Spring Boot Actuator** - Monitoring and management

## 📋 Prerequisites

- **Java 21** or higher
- **PostgreSQL 12+** running on localhost:5432
- **Maven 3.6+** (or use included wrapper)

## 🔧 Environment Variables

Set the following environment variables for database connection:

```bash
PGHOST=localhost          # PostgreSQL host
PGPORT=5432              # PostgreSQL port
PGDATABASE=postgres      # Database name
PGUSER=postgres          # Database username
PGPASSWORD=yourpassword  # Database password
```

## ⚡ Quick Start

### 1. Clone and Navigate
```bash
git clone <repository-url>
cd content-calendar/backend
```

### 2. Set Environment Variables
```bash
# Windows
set PGHOST=localhost
set PGPORT=5432
set PGDATABASE=postgres
set PGUSER=postgres
set PGPASSWORD=yourpassword

# Linux/Mac
export PGHOST=localhost
export PGPORT=5432
export PGDATABASE=postgres
export PGUSER=postgres
export PGPASSWORD=yourpassword
```

### 3. Run the Application
```bash
# Using Maven wrapper (recommended)
./mvnw spring-boot:run

# Or using installed Maven
mvn spring-boot:run

# Or build and run JAR
./mvnw clean package
java -jar target/backend-0.0.1-SNAPSHOT.jar
```

### 4. Verify Installation
- Application runs on: `http://localhost:8080`
- Health check: `http://localhost:8080/actuator/health`
- Home endpoint: `http://localhost:8080/`

## 📡 API Endpoints

### Home
- `GET /` - Get application configuration and welcome message

### Content Management
- `GET /api/content` - Get all content
- `GET /api/content/{id}` - Get content by ID
- `POST /api/content` - Create new content
- `PUT /api/content/{id}` - Update existing content
- `DELETE /api/content/{id}` - Delete content

### Search & Filter
- `GET /api/content/filter/{keyword}` - Search content by title keyword
- `GET /api/content/filter/status/{status}` - Filter content by status

### Monitoring
- `GET /actuator/health` - Application health status
- `GET /actuator/info` - Application information

## 📊 Data Models

### Content
```json
{
  "id": 1,
  "title": "My Article Title",
  "desc": "Article description",
  "status": "IN_PROGRESS",
  "contentType": "ARTICLE",
  "dateCreated": "2025-09-15T10:00:00",
  "dateUpdated": "2025-09-15T11:00:00",
  "url": "https://example.com/my-article"
}
```

### Status Enum
- `IDEA` - Initial concept
- `IN_PROGRESS` - Currently being worked on
- `COMPLETED` - Finished but not published
- `PUBLISHED` - Live and available

### Content Type Enum
- `ARTICLE` - Blog posts, articles
- `VIDEO` - Video content
- `COURSE` - Educational courses
- `CONFERENCE_TALK` - Presentations and talks

## 🔨 API Usage Examples

### Create Content
```bash
curl -X POST http://localhost:8080/api/content \
  -H "Content-Type: application/json" \
  -d '{
    "title": "Spring Boot Tutorial",
    "desc": "Complete guide to Spring Boot",
    "status": "IDEA",
    "contentType": "ARTICLE",
    "dateCreated": "2025-09-15T10:00:00",
    "url": ""
  }'
```

### Update Content
```bash
curl -X PUT http://localhost:8080/api/content/1 \
  -H "Content-Type: application/json" \
  -d '{
    "title": "Spring Boot Advanced Tutorial",
    "desc": "Advanced Spring Boot concepts",
    "status": "IN_PROGRESS",
    "contentType": "ARTICLE",
    "dateCreated": "2025-09-15T10:00:00",
    "dateUpdated": "2025-09-15T12:00:00",
    "url": ""
  }'
```

### Search Content
```bash
# Search by title
curl http://localhost:8080/api/content/filter/Spring

# Filter by status
curl http://localhost:8080/api/content/filter/status/IN_PROGRESS
```

## 🗄️ Database Schema

```sql
CREATE TABLE content (
    id SERIAL PRIMARY KEY,
    title VARCHAR(255) NOT NULL,
    description TEXT,
    status VARCHAR(20) NOT NULL,
    content_type VARCHAR(50) NOT NULL,
    date_created TIMESTAMP NOT NULL,
    date_updated TIMESTAMP,
    url VARCHAR(255)
);
```

## 🏗️ Project Structure

```
src/
├── main/
│   ├── java/com/backend/backend/
│   │   ├── Application.java                 # Main application class
│   │   ├── config/
│   │   │   ├── ContentCalendarProperties.java  # Configuration properties
│   │   │   └── DataLoader.java              # Sample data loader
│   │   ├── controller/
│   │   │   ├── HomeController.java          # Home endpoint
│   │   │   └── ContentController.java       # Content CRUD operations
│   │   ├── model/
│   │   │   ├── Content.java                 # Content entity
│   │   │   ├── Status.java                  # Status enumeration
│   │   │   └── Type.java                    # Content type enumeration
│   │   └── repository/
│   │       ├── ContentRepository.java       # Main repository interface
│   │       ├── ContentCollectionRepository.java  # In-memory implementation
│   │       └── ContentJdbcTemplateRepository.java # JDBC implementation
│   └── resources/
│       ├── application.properties           # Configuration
│       ├── schema.sql                       # Database schema
│       └── data/
│           └── content.json                 # Sample data
└── test/
    └── java/com/backend/backend/
        └── ApplicationTests.java            # Basic tests
```



### Application Properties
```properties
# Database Configuration
spring.datasource.url=jdbc:postgresql://${PGHOST}:${PGPORT}/${PGDATABASE}
spring.datasource.username=${PGUSER}
spring.datasource.password=${PGPASSWORD}
spring.datasource.driver-class-name=org.postgresql.Driver

# Schema Initialization
spring.sql.init.mode=always

# Custom Properties
cc.welcomeMessage=HELLO FROM THE OTHER SIDE!
cc.about=SMILE !!!
```

### Environment-Specific Configuration
Create additional property files for different environments:
- `application-dev.properties` - Development
- `application-prod.properties` - Production
- `application-test.properties` - Testing

## 🚀 Deployment

### Local Development
```bash
./mvnw spring-boot:run
```

### Production Build
```bash
./mvnw clean package
java -jar target/backend-0.0.1-SNAPSHOT.jar
```

### Docker (Optional)
```dockerfile
FROM openjdk:21-jre-slim
COPY target/backend-0.0.1-SNAPSHOT.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "/app.jar"]
```

## 🔍 Monitoring

### Health Checks
- `GET /actuator/health` - Application health
- `GET /actuator/info` - Application information

### Logging
Application uses Spring Boot's default logging configuration. Logs are output to console by default.

## 🐛 Troubleshooting

### Common Issues

**Database Connection Failed**
- Verify PostgreSQL is running
- Check environment variables are set correctly
- Ensure database exists and user has permissions

**Port Already in Use**
- Change server port: `server.port=8081` in application.properties
- Kill process using port 8080: `lsof -ti:8080 | xargs kill -9` (Mac/Linux)

**Tests Failing**
- Ensure test database is configured
- Check H2 dependency is available for tests
- Verify test application properties are correct

## 🤝 Contributing

1. Fork the repository
2. Create a feature branch: `git checkout -b feature/new-feature`
3. Commit changes: `git commit -am 'Add new feature'`
4. Push to branch: `git push origin feature/new-feature`
5. Submit a Pull Request

## 📄 License

This project is licensed under the MIT License - see the LICENSE file for details.

## 🙏 Acknowledgments

- Spring Boot team for the excellent framework
- PostgreSQL community for the robust database
- All contributors and users of this project

---

**Happy Content Creating! 🎉**

For questions or support, please open an issue in the repository.
