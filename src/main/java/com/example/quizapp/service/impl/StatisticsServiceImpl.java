package com.example.quizapp.service.impl;

import com.example.quizapp.dto.QuizDashboardDTO;
import com.example.quizapp.dto.QuizStatsDTO;
import com.example.quizapp.model.QuizSession;
import com.example.quizapp.model.UserResponse;
import com.example.quizapp.repository.QuizSessionRepository;
import com.example.quizapp.repository.UserResponseRepository;
import com.example.quizapp.service.interfaces.SessionService;
import com.example.quizapp.service.interfaces.StatisticsService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class StatisticsServiceImpl implements StatisticsService {
    private static final Logger logger = LoggerFactory.getLogger(StatisticsServiceImpl.class);

    private final UserResponseRepository userResponseRepository;
    private final QuizSessionRepository quizSessionRepository;
    private final SessionService sessionService;

    @Override
    public QuizStatsDTO getSessionStats(Long sessionId) {
        QuizSession session = sessionService.validateAndUpdateSession(sessionId);
        List<UserResponse> responses = userResponseRepository.findByQuizSession(session);

        int totalQuestions = responses.size();
        long correctAnswers = responses.stream()
                .filter(UserResponse::isCorrect)
                .count();

        QuizStatsDTO stats = new QuizStatsDTO();
        stats.setTotalQuestions(totalQuestions);
        stats.setCorrectAnswers((int) correctAnswers);
        stats.setIncorrectAnswers(totalQuestions - (int) correctAnswers);

        if (totalQuestions > 0) {
            double accuracy = (double) correctAnswers / totalQuestions * 100;
            stats.setAccuracyPercentage(Math.round(accuracy * 100.0) / 100.0);
        }

        return stats;
    }

    @Override
    public QuizDashboardDTO getDashboard(Long sessionId) {
        QuizSession session = sessionService.validateAndUpdateSession(sessionId);
        List<UserResponse> responses = userResponseRepository.findByQuizSession(session);

        QuizDashboardDTO dashboard = new QuizDashboardDTO();

        dashboard.setTotalSessions(quizSessionRepository.count());
        dashboard.setQuestionsAttempted(responses.size());

        calculateAccuracies(dashboard, responses);

        calculateTimeStats(dashboard, responses);

        dashboard.setRecentPerformance(calculateRecentPerformance(responses));

        return dashboard;
    }

    private void calculateAccuracies(QuizDashboardDTO dashboard, List<UserResponse> responses) {
        if (responses.isEmpty()) {
            dashboard.setOverallAccuracy(0.0);
            return;
        }

        long correctAnswers = responses.stream()
                .filter(UserResponse::isCorrect)
                .count();
        dashboard.setOverallAccuracy((double) correctAnswers / responses.size() * 100);

        Map<String, Double> categoryAccuracy = responses.stream()
                .collect(Collectors.groupingBy(
                        r -> r.getQuestion().getCategory().toString(),
                        Collectors.averagingDouble(r -> r.isCorrect() ? 100 : 0)
                ));
        dashboard.setCategoryAccuracy(categoryAccuracy);

        String bestCategory = categoryAccuracy.entrySet().stream()
                .max(Map.Entry.comparingByValue())
                .map(Map.Entry::getKey)
                .orElse("No data");
        dashboard.setBestCategory(bestCategory);

        Map<String, Double> difficultyAccuracy = responses.stream()
                .collect(Collectors.groupingBy(
                        r -> r.getQuestion().getDifficulty().toString(),
                        Collectors.averagingDouble(r -> r.isCorrect() ? 100 : 0)
                ));
        String bestDifficulty = difficultyAccuracy.entrySet().stream()
                .max(Map.Entry.comparingByValue())
                .map(Map.Entry::getKey)
                .orElse("No data");
        dashboard.setBestDifficulty(bestDifficulty);
    }

    private void calculateTimeStats(QuizDashboardDTO dashboard, List<UserResponse> responses) {
        if (responses.isEmpty()) return;

        double avgTime = responses.stream()
                .mapToInt(UserResponse::getResponseTime)
                .average()
                .orElse(0);
        dashboard.setAverageResponseTime(avgTime);

        int inTimeCount = (int) responses.stream()
                .filter(r -> r.getResponseTime() <= r.getQuestion().getTimeLimit())
                .count();
        dashboard.setQuestionsAnsweredInTime(inTimeCount);
        dashboard.setTotalQuestionsWithTimer(responses.size());
    }

    private String calculateRecentPerformance(List<UserResponse> responses) {
        if (responses.size() < 3) {
            return "Not enough data (need 3 questions)";
        }

        List<UserResponse> recent = responses.subList(0, Math.min(3, responses.size()));
        double recentAccuracy = recent.stream()
                .filter(UserResponse::isCorrect)
                .count() * 100.0 / recent.size();

        if (recentAccuracy >= 70) return "Good Start!";
        if (recentAccuracy >= 50) return "Keep Practicing";
        return "Needs Focus";
    }
}