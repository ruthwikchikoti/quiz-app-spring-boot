package com.example.quizapp.model;

import com.example.quizapp.enums.Category;
import com.example.quizapp.enums.Difficulty;
import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "questions")
public class Question {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "question_text", nullable = false)
    private String questionText;

    @Column(name = "optiona", nullable = false)
    private String optionA;

    @Column(name = "optionb", nullable = false)
    private String optionB;

    @Column(name = "optionc", nullable = false)
    private String optionC;

    @Column(name = "optiond", nullable = false)
    private String optionD;

    @Column(name = "correct_answer", nullable = false)
    private String correctAnswer;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Difficulty difficulty;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Category category;

    @Column(name = "time_limit", nullable = false)
    private Integer timeLimit = 20; 
}