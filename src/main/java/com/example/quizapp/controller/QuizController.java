package com.example.quizapp.controller;

import com.example.quizapp.dto.*;
import com.example.quizapp.exception.QuizException;
import com.example.quizapp.service.interfaces.AnswerService;
import com.example.quizapp.service.interfaces.QuestionService;
import com.example.quizapp.service.interfaces.SessionService;
import com.example.quizapp.service.interfaces.StatisticsService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/quiz")
@RequiredArgsConstructor
public class QuizController {
    private static final Logger logger = LoggerFactory.getLogger(QuizController.class);

    private final SessionService sessionService;
    private final QuestionService questionService;
    private final AnswerService answerService;
    private final StatisticsService statisticsService;

    @PostMapping("/start")
    public ResponseEntity<QuizSessionDTO> startNewSession() {
        logger.info("Request to start new quiz session");
        return ResponseEntity.ok(sessionService.startNewSession());
    }

    @GetMapping("/question")
    public ResponseEntity<QuestionDTO> getQuestion(@RequestParam Long sessionId) {
        if (sessionId == null) {
            throw new QuizException("Session ID is required");
        }
        logger.info("Request to get question for session: {}", sessionId);
        return ResponseEntity.ok(questionService.getNextQuestion(sessionId));
    }

    @PostMapping("/submit")
    public ResponseEntity<Boolean> submitAnswer(@RequestBody AnswerSubmissionDTO submission) {
        if (submission.getSessionId() == null || submission.getAnswer() == null) {
            throw new QuizException("Session ID and answer are required");
        }
        logger.info("Request to submit answer for session: {}", submission.getSessionId());
        return ResponseEntity.ok(answerService.submitAnswer(
                submission.getSessionId(),
                submission.getAnswer()
        ));
    }

    @GetMapping("/stats")
    public ResponseEntity<QuizStatsDTO> getStats(@RequestParam Long sessionId) {
        if (sessionId == null) {
            throw new QuizException("Session ID is required");
        }
        logger.info("Request to get stats for session: {}", sessionId);
        return ResponseEntity.ok(statisticsService.getSessionStats(sessionId));
    }

    @GetMapping("/dashboard")
    public ResponseEntity<QuizDashboardDTO> getDashboard(@RequestParam Long sessionId) {
        if (sessionId == null) {
            throw new QuizException("Session ID is required");
        }
        logger.info("Request to get dashboard for session: {}", sessionId);
        return ResponseEntity.ok(statisticsService.getDashboard(sessionId));
    }
}