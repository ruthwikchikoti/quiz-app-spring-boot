package com.example.quizapp.model;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "quiz_sessions")
public class QuizSession {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private LocalDateTime startTime;

    @Column(nullable = false)
    private LocalDateTime lastAccessTime;

    @Column(nullable = false)
    private boolean active = true;

    @Column(nullable = false)
    private int totalQuestions = 0;

    @Column(nullable = false)
    private int correctAnswers = 0;
}