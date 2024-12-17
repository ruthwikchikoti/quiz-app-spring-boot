package com.example.quizapp.model;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "user_responses")
public class UserResponse {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "session_id", nullable = false)
    private QuizSession quizSession;

    @ManyToOne
    @JoinColumn(name = "question_id", nullable = false)
    private Question question;

    @Column(nullable = false)
    private String userAnswer;

    @Column(nullable = false)
    private boolean correct;

    @Column(nullable = false)
    private LocalDateTime answeredAt;

    @Column(name = "response_time")
    private Integer responseTime; // in seconds
}