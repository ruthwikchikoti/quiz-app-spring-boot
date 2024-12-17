package com.example.quizapp.service.interfaces;

import com.example.quizapp.model.UserResponse;

public interface AnswerService {
    boolean submitAnswer(Long sessionId, String answer);
    UserResponse saveUserResponse(Long sessionId, String answer, boolean isCorrect);
}