# Mock Interview Request Platform

A local MVP for the following flow:

1. Candidate opens the request form.
2. Candidate enters profile, target role, experience and requirements.
3. Candidate uploads a resume and preferred interview slots.
4. System creates a unique request ID and starts in `PENDING`.
5. Suitable interviewers are displayed using simple role/skill matching.
6. Interviewer can accept or decline.
7. After a decline, another suitable interviewer can be selected.
8. Interviewer confirms a preferred slot and meeting link.
9. Status becomes `CONFIRMED`.
10. Interview can be marked `COMPLETED`.

## Technology

- Java 21
- Spring Boot 3.5.6
- Spring MVC + Thymeleaf
- Spring Data JPA
- H2 for local development
- MySQL driver included for the later Docker Compose setup
- Maven

## Important MVP note

This project intentionally has **no authentication/authorization** because the supplied flow did not define login/roles. The interviewer dashboard is therefore a demo/admin-style page. Add Spring Security, candidate/interviewer identities and authorization before production use.

## Local run

Install Java 21 and Maven.

From the project root:

```bash
mvn clean spring-boot:run
```

or:

```bash
mvn clean package
java -jar target/mock-interview-request-platform-1.0.0.jar
```

Open:

- http://localhost:8080/
- http://localhost:8080/requests/new
- http://localhost:8080/interviewer/dashboard
- http://localhost:8080/actuator/health
- H2 console: http://localhost:8080/h2-console

H2 JDBC URL:

```text
jdbc:h2:file:./data/mockinterview
```

User: `sa`
Password: empty

## Local data

- H2 database files: `./data/`
- Uploaded resumes: `./uploads/resumes/`

These directories are created automatically.

## MySQL profile for later containerization

The project includes `application-mysql.properties`.

Run with:

```bash
mvn spring-boot:run -Dspring-boot.run.profiles=mysql
```

Environment variables:

```text
DB_URL=jdbc:mysql://localhost:3306/mock_interview?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC
DB_USERNAME=mockuser
DB_PASSWORD=mockpassword
APP_UPLOAD_DIR=uploads/resumes
SERVER_PORT=8080
```

The current project is intentionally **not Dockerized**. After validating it locally, create a Dockerfile and Docker Compose file using the successful local build/run behavior as the baseline.

## Main routes

- `GET /` — home and request list
- `GET /requests/new` — candidate form
- `POST /requests` — create request + upload resume
- `GET /requests/{requestId}` — request details
- `GET /requests/{requestId}/select-interviewer` — interviewer selection
- `POST /requests/{requestId}/select-interviewer` — select interviewer
- `GET /requests/{requestId}/confirm` — confirmation page
- `POST /requests/{requestId}/confirm` — confirm slot and meeting link
- `POST /requests/{requestId}/complete` — mark completed
- `GET /interviewer/dashboard` — demo interviewer dashboard
- `POST /interviewer/requests/{id}/accept` — accept
- `POST /interviewer/requests/{id}/decline` — decline

## Suggested next production steps

1. Add authentication and role-based authorization.
2. Add real interviewer availability rather than only candidate-preferred slots.
3. Add email/notification integration.
4. Add a proper object store for resumes if needed.
5. Add database migrations with Flyway.
6. Add audit logging and validation.
7. Add tests.
8. Add Dockerfile and Docker Compose after local validation.
