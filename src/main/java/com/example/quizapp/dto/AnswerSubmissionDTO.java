package com.example.quizapp.dto;

import lombok.Data;

@Data
public class AnswerSubmissionDTO {
    private Long sessionId;
    private String answer;
}
