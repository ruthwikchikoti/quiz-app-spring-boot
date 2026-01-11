package com.example.quizapp.service.interfaces;

import com.example.quizapp.dto.QuizSessionDTO;
import com.example.quizapp.model.QuizSession;

public interface SessionService {
    QuizSessionDTO startNewSession();
    QuizSession validateAndUpdateSession(Long sessionId);
    void saveSession(QuizSession session);
    void cleanupInactiveSessions();
}