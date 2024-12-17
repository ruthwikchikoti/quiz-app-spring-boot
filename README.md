# Spring Boot Quiz Application

A robust REST API-based Quiz Application built with Spring Boot that offers real-time quiz sessions with advanced analytics and performance tracking.

## Features

### Core Functionality
- Dynamic quiz session management
- Random question generation from database
- Multiple choice questions with instant feedback
- Comprehensive session statistics

### Advanced Features
1. **Question Management**
   - Category-based questions (Science, Technology, Geography, etc.)
   - Difficulty levels (Easy, Medium, Hard)
   - Time limit per question
   - Multiple choice format with single correct answer

2. **Session Handling**
   - Unique session tracking
   - 30-minute session timeout
   - Automatic inactive session cleanup
   - Concurrent session support

3. **Performance Analytics**
   - Real-time accuracy tracking
   - Category-wise performance analysis
   - Difficulty-level progression
   - Response time monitoring
   - Performance trend analysis
   - Detailed session statistics

4. **Advanced Statistics**
   - Overall accuracy metrics
   - Category-wise success rates
   - Time-based performance analysis
   - Best performing categories
   - Recent performance indicators

## Technical Stack

- **Framework**: Spring Boot 3.1.5
- **Language**: Java 17
- **Database**: H2 (In-memory)
- **Build Tool**: Maven
- **Documentation**: OpenAPI (Swagger)
- **Testing**: JUnit 5
- **Logging**: SLF4J with Logback

## Project Structure
```
src/
├── main/
│   ├── java/
│   │   └── com/example/quizapp/
│   │       ├── controller/
│   │       │   └── QuizController.java
│   │       ├── dto/
│   │       │   ├── AnswerSubmissionDTO.java
│   │       │   ├── QuestionDTO.java
│   │       │   ├── QuizDashboardDTO.java
│   │       │   ├── QuizSessionDTO.java
│   │       │   └── QuizStatsDTO.java
│   │       ├── enums/
│   │       │   ├── Category.java
│   │       │   └── Difficulty.java
│   │       ├── exception/
│   │       │   ├── GlobalExceptionHandler.java
│   │       │   └── QuizException.java
│   │       ├── model/
│   │       │   ├── Question.java
│   │       │   ├── QuizSession.java
│   │       │   └── UserResponse.java
│   │       ├── repository/
│   │       │   ├── QuestionRepository.java
│   │       │   ├── QuizSessionRepository.java
│   │       │   └── UserResponseRepository.java
│   │       ├── service/
│   │       │   ├── impl/
│   │       │   │   ├── AnswerServiceImpl.java
│   │       │   │   ├── QuestionServiceImpl.java
│   │       │   │   ├── SessionServiceImpl.java
│   │       │   │   └── StatisticsServiceImpl.java
│   │       │   └── interfaces/
│   │       │       ├── AnswerService.java
│   │       │       ├── QuestionService.java
│   │       │       ├── SessionService.java
│   │       │       └── StatisticsService.java
│   │       └── QuizApplication.java
│   └── resources/
│       ├── application.properties
│       ├── data.sql
│       └── schema.sql
```

## API Endpoints

### 1. Session Management
```http
POST /api/quiz/start
Response: {
    "sessionId": "1234",
    "message": "New quiz session started successfully!"
}
```

### 2. Question Retrieval
```http
GET /api/quiz/question?sessionId={sessionId}
Response: {
    "questionText": "What is the capital of France?",
    "optionA": "London",
    "optionB": "Berlin",
    "optionC": "Paris",
    "optionD": "Madrid",
    "category": "GEOGRAPHY",
    "difficulty": "EASY",
    "timeLimit": 20,
    "timestamp": 1734455573950
}
```

### 3. Answer Submission
```http
POST /api/quiz/submit
Request Body: {
    "sessionId": "1234",
    "answer": "C"
}
Response: true/false
```

### 4. Statistics
```http
GET /api/quiz/stats?sessionId={sessionId}
Response: {
    "totalQuestions": 10,
    "correctAnswers": 7,
    "incorrectAnswers": 3,
    "accuracyPercentage": 70.0
}
```

### 5. Dashboard
```http
GET /api/quiz/dashboard?sessionId={sessionId}
Response: {
    "totalSessions": 5,
    "questionsAttempted": 10,
    "categoryAccuracy": {
        "SCIENCE": 80.0,
        "GEOGRAPHY": 70.0
    },
    "overallAccuracy": 75.0,
    "bestCategory": "SCIENCE",
    "bestDifficulty": "EASY",
    "recentPerformance": "Improving",
    "averageResponseTime": 15.5,
    "questionsAnsweredInTime": 8,
    "totalQuestionsWithTimer": 10
}
```

## Setup Instructions

1. **Prerequisites**
   - Java 17 or higher
   - Maven 3.6 or higher
   - IDE (IntelliJ IDEA recommended)

2. **Clone and Build**
   ```bash
   git clone https://github.com/ruthwikchikoti/quiz-app-spring-boot
   cd quiz-app
   mvn clean install
   ```

3. **Run Application**
   ```bash
   mvn spring-boot:run
   ```

4. **Access H2 Console**
   - URL: http://localhost:8080/h2-console
   - JDBC URL: jdbc:h2:mem:quizdb
   - Username: sa
   - Password: password

## Design Patterns & Best Practices

- **Service Layer Pattern**: Separating business logic from controllers
- **DTO Pattern**: Clean data transfer between layers
- **Repository Pattern**: Database interaction abstraction
- **Exception Handling**: Global exception handling with custom exceptions
- **Logging**: Comprehensive logging with SLF4J
- **Transactional Management**: Ensuring data consistency
- **Input Validation**: Request validation at controller level

## Assumptions

1. Single user system (no authentication required)
2. Questions are pre-seeded in the database
3. Each quiz session has a 30-minute timeout
4. All questions have equal weightage
5. Time limit of 20 seconds per question
6. Answers are case-insensitive
7. Questions can be repeated across different sessions
