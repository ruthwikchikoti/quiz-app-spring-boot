# Spring Boot Quiz Application

[![Quiz App CI](https://github.com/ruthwikchikoti/quiz-app-spring-boot/workflows/Quiz%20App%20CI/badge.svg)](https://github.com/ruthwikchikoti/quiz-app-spring-boot/actions/workflows/ci.yml)
[![CodeQL Security Scan](https://github.com/ruthwikchikoti/quiz-app-spring-boot/workflows/CodeQL%20Security%20Scan/badge.svg)](https://github.com/ruthwikchikoti/quiz-app-spring-boot/actions/workflows/codeql.yml)
[![Docker Image](https://img.shields.io/badge/docker-ruthwikchikoti%2Fquiz--app-blue)](https://hub.docker.com/r/ruthwikchikoti/quiz-app)
[![Java Version](https://img.shields.io/badge/Java-17-orange)](https://adoptium.net/)
[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.1.5-brightgreen)](https://spring.io/projects/spring-boot)

A robust REST API-based Quiz Application built with Spring Boot that offers real-time quiz sessions with advanced analytics and performance tracking. Features a production-grade CI/CD pipeline with automated security scanning, testing, and deployment.

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
quiz-app-spring-boot/
├── .github/workflows/
│   ├── ci.yml                  # Main CI pipeline
│   └── codeql.yml              # SAST security scan
├── k8s/
│   ├── namespace.yaml          # K8s namespace
│   ├── configmap.yaml          # Spring configuration
│   ├── deployment.yaml         # Pod deployment (2 replicas)
│   ├── service.yaml            # NodePort service
│   └── kustomization.yaml      # Kustomize bundle
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── com/example/quizapp/
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
│   │       │       └── QuizApplication.java
│   │   └── resources/
│   │       ├── application.properties
│   │       ├── data.sql
│   │       └── schema.sql
│   └── test/                   # Unit tests
├── Dockerfile                  # Container definition
├── pom.xml                     # Maven dependencies
├── checkstyle.xml              # Code quality rules
└── README.md                   # This file
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

---

## CI/CD Pipeline

### Overview

This project implements a **production-grade CI/CD pipeline** using GitHub Actions with multiple security layers, automated testing, and containerization.

### Pipeline Stages

```
Code Push → Checkout → Setup Java → Checkstyle → Unit Tests → Build 
   ↓
SCA Scan (Trivy) → Docker Build → Container Scan (Trivy) → Smoke Test
   ↓
DockerHub Login → Push Image ✅
```

**Parallel Workflow:** CodeQL SAST analysis runs simultaneously

### Security Scanning

| Type | Tool | Purpose |
|------|------|---------|
| **Code Quality** | Checkstyle | Google Java Style enforcement |
| **SAST** | CodeQL | Static code security analysis |
| **SCA** | Trivy | Dependency vulnerability scanning |
| **Container** | Trivy | Docker image security scanning |

**Current Status:** ✅ **0 CRITICAL vulnerabilities**

### Workflows

1. **Quiz App CI** (`.github/workflows/ci.yml`)
   - Trigger: Push to `main` + manual (`workflow_dispatch`)
   - Duration: ~5-7 minutes
   - Stages: 11 automated steps
   - Output: Docker image on DockerHub

2. **CodeQL Security Scan** (`.github/workflows/codeql.yml`)
   - Trigger: Push to `main` + Pull Requests
   - Duration: ~2-3 minutes
   - Output: Security findings in GitHub Security tab

### Running the Pipeline Locally

**Prerequisites:**
- Docker installed
- DockerHub account (for pushing images)

**Build & Test:**
```bash
# Run code quality check
./mvnw validate

# Run unit tests
./mvnw test

# Build application
./mvnw clean package

# Build Docker image
docker build -t quiz-app:latest .

# Run container locally
docker run -d -p 8080:8080 --name quiz quiz-app:latest

# Test the application
curl http://localhost:8080

# Stop container
docker stop quiz && docker rm quiz
```

**Security Scanning:**
```bash
# Install Trivy (macOS)
brew install trivy

# Scan dependencies
trivy fs .

# Scan Docker image
trivy image quiz-app:latest
```

### GitHub Secrets Configuration

For the CI/CD pipeline to work, configure these secrets in your GitHub repository:

**Settings → Secrets and variables → Actions → New repository secret**

| Secret Name | Description | How to Get |
|-------------|-------------|------------|
| `DOCKERHUB_USERNAME` | Your DockerHub username | Your DockerHub account name |
| `DOCKERHUB_TOKEN` | DockerHub access token | DockerHub → Account Settings → Security → New Access Token |

**Creating DockerHub Access Token:**
1. Login to [DockerHub](https://hub.docker.com)
2. Go to **Account Settings** → **Security**
3. Click **New Access Token**
4. Description: `GitHub Actions CI`
5. Permissions: **Read, Write, Delete**
6. Copy the token immediately (shown only once)
7. Add to GitHub Secrets as `DOCKERHUB_TOKEN`

### CI/CD Features

✅ **Automated Build** - Compiles and packages on every push  
✅ **Code Quality Gates** - Checkstyle enforces coding standards  
✅ **Automated Testing** - Unit tests run before build  
✅ **Security Scanning** - Multiple layers (SAST, SCA, Container)  
✅ **Container Validation** - Smoke tests ensure deployability  
✅ **Registry Publishing** - Automatic push to DockerHub  
✅ **Fail-Fast** - Pipeline stops on first failure  
✅ **Secrets Management** - Secure credential handling  

### Pipeline Architecture

```
┌─────────────────────────────────────────────────────────────┐
│                      GitHub Actions Runner                   │
├─────────────────────────────────────────────────────────────┤
│  1. Checkout Code                                           │
│  2. Setup Java 17 (Eclipse Temurin)                         │
│  3. Code Quality Check (Checkstyle)                         │
│  4. Unit Tests (JUnit)                                       │
│  5. Build Application (Maven)                                │
│  6. SCA Scan (Trivy - Dependencies)         [Security Gate] │
│  7. Build Docker Image                                       │
│  8. Container Scan (Trivy - Image)          [Security Gate] │
│  9. Runtime Smoke Test (curl)                               │
│ 10. Login to DockerHub (GitHub Secrets)                     │
│ 11. Push Image to Registry                                  │
└─────────────────────────────────────────────────────────────┘

┌─────────────────────────────────────────────────────────────┐
│                 Parallel: CodeQL SAST Workflow               │
├─────────────────────────────────────────────────────────────┤
│  • Checkout Code                                            │
│  • Initialize CodeQL (Java)                                  │
│  • Build Project                                             │
│  • Perform Security Analysis                                 │
│  • Upload Results to GitHub Security Tab                    │
└─────────────────────────────────────────────────────────────┘
```

### Deployment

**Pull from DockerHub:**
```bash
docker pull ruthwikchikoti/quiz-app:latest
docker run -p 8080:8080 ruthwikchikoti/quiz-app:latest
```

---

## Kubernetes Deployment

This project includes production-ready Kubernetes manifests for local deployment.

### K8s Manifest Files

```
k8s/
├── namespace.yaml      # Resource isolation (quiz-app namespace)
├── configmap.yaml      # Externalized Spring configuration
├── deployment.yaml     # 2 replicas with health probes
├── service.yaml        # NodePort service (port 30080)
└── kustomization.yaml  # Single-command deployment
```

### K8s Architecture

```
┌─────────────────────────────────────────────────────────┐
│                    MINIKUBE CLUSTER                      │
│  ┌───────────────────────────────────────────────────┐  │
│  │                Namespace: quiz-app                 │  │
│  │                                                    │  │
│  │  ┌──────────────────────────────────────────────┐ │  │
│  │  │           Service (NodePort:30080)            │ │  │
│  │  └─────────────────────┬────────────────────────┘ │  │
│  │                        │                           │  │
│  │          ┌─────────────┴─────────────┐            │  │
│  │          ▼                           ▼            │  │
│  │  ┌──────────────┐           ┌──────────────┐     │  │
│  │  │    Pod 1     │           │    Pod 2     │     │  │
│  │  │   quiz-app   │           │   quiz-app   │     │  │
│  │  └──────────────┘           └──────────────┘     │  │
│  │                                                    │  │
│  └───────────────────────────────────────────────────┘  │
└─────────────────────────────────────────────────────────┘
```

### Deploy to Kubernetes (Minikube)

**Prerequisites:**
- [minikube](https://minikube.sigs.k8s.io/docs/start/) installed
- [kubectl](https://kubernetes.io/docs/tasks/tools/) installed

**Deployment Steps:**
```bash
# Start minikube
minikube start --driver=docker

# Deploy all resources with kustomize
kubectl apply -k k8s/

# Wait for pods to be ready (~90 seconds for Spring Boot)
kubectl get pods -n quiz-app -w

# Get service URL
minikube service quiz-app-service -n quiz-app --url

# Test the application
curl -X POST http://<MINIKUBE-IP>:30080/api/quiz/start
```

**Useful Commands:**
```bash
# View all resources
kubectl get all -n quiz-app

# View pod logs
kubectl logs -n quiz-app -l app=quiz-app

# Scale deployment
kubectl scale deployment quiz-app -n quiz-app --replicas=3

# Cleanup
kubectl delete -k k8s/
minikube stop
```

### K8s Configuration Highlights

| Feature | Configuration | Why |
|---------|---------------|-----|
| **Replicas** | 2 | High availability |
| **Probes** | TCP on port 8080 | App has no health endpoint |
| **Initial Delay** | 90 seconds | Spring Boot startup time |
| **Resource Limits** | 512Mi memory, 500m CPU | Prevents resource starvation |
| **Service Type** | NodePort (30080) | Local access with minikube |

### Pipeline Metrics

| Metric | Value |
|--------|-------|
| **Total Runtime** | 5-7 minutes |
| **Success Rate** | 95%+ |
| **Security Scans** | 4 layers (Checkstyle, CodeQL, SCA, Container) |
| **Vulnerabilities** | 0 CRITICAL, 21 HIGH (monitored) |
| **Container Size** | ~350 MB |
| **JAR Size** | ~48 MB |

---

## Assumptions

1. Single user system (no authentication required)
2. Questions are pre-seeded in the database
3. Each quiz session has a 30-minute timeout
4. All questions have equal weightage
5. Time limit of 20 seconds per question
6. Answers are case-insensitive
7. Questions can be repeated across different sessions
