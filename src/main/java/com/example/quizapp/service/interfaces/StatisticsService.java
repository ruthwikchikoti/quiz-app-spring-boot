package com.example.quizapp.service.interfaces;

import com.example.quizapp.dto.QuizDashboardDTO;
import com.example.quizapp.dto.QuizStatsDTO;

public interface StatisticsService {
    QuizStatsDTO getSessionStats(Long sessionId);
    QuizDashboardDTO getDashboard(Long sessionId);
}