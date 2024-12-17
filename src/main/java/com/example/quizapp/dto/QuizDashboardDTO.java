package com.example.quizapp.dto;

import lombok.Data;
import java.util.Map;

@Data
public class QuizDashboardDTO {
    private long totalSessions;
    private int questionsAttempted;
    private Map<String, Double> categoryAccuracy;
    private double overallAccuracy;
    private String bestCategory;
    private String bestDifficulty;
    private String recentPerformance;  
    private double averageResponseTime;
    private int questionsAnsweredInTime;
    private int totalQuestionsWithTimer;
}