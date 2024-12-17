package com.example.quizapp.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class QuizSessionDTO {
    private Long sessionId;
    private String message;
}