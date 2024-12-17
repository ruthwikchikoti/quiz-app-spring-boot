package com.example.quizapp.service.impl;

import com.example.quizapp.dto.QuizSessionDTO;
import com.example.quizapp.exception.QuizException;
import com.example.quizapp.model.QuizSession;
import com.example.quizapp.repository.QuizSessionRepository;
import com.example.quizapp.service.interfaces.SessionService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class SessionServiceImpl implements SessionService {
    private static final Logger logger = LoggerFactory.getLogger(SessionServiceImpl.class);

    private final QuizSessionRepository quizSessionRepository;

    @Value("${quiz.session.timeout.minutes:30}")
    private int sessionTimeoutMinutes;

    @Override
    @Transactional
    public QuizSessionDTO startNewSession() {
        logger.debug("Starting new quiz session");
        try {
            QuizSession session = new QuizSession();
            session.setStartTime(LocalDateTime.now());
            session.setLastAccessTime(LocalDateTime.now());
            session.setActive(true);
            session.setTotalQuestions(0);
            session.setCorrectAnswers(0);

            QuizSession savedSession = quizSessionRepository.save(session);
            logger.info("New quiz session created with ID: {}", savedSession.getId());

            QuizSessionDTO dto = new QuizSessionDTO();
            dto.setSessionId(savedSession.getId());
            dto.setMessage("New quiz session started successfully!");
            return dto;
        } catch (Exception e) {
            logger.error("Error creating new session", e);
            throw new QuizException("Failed to create new quiz session");
        }
    }

    @Override
    @Transactional
    public QuizSession validateAndUpdateSession(Long sessionId) {
        if (sessionId == null) {
            logger.error("Session ID is null");
            throw new QuizException("Session ID is required");
        }

        QuizSession session = quizSessionRepository.findById(sessionId)
                .orElseThrow(() -> {
                    logger.error("Invalid session ID: {}", sessionId);
                    return new QuizException("Invalid session ID: " + sessionId);
                });

        if (!session.isActive()) {
            logger.warn("Attempt to access expired session: {}", sessionId);
            throw new QuizException("Session has expired");
        }

        LocalDateTime timeoutThreshold = LocalDateTime.now().minusMinutes(sessionTimeoutMinutes);
        if (session.getLastAccessTime().isBefore(timeoutThreshold)) {
            session.setActive(false);
            quizSessionRepository.save(session);
            logger.warn("Session timed out: {}", sessionId);
            throw new QuizException("Session has timed out");
        }

        session.setLastAccessTime(LocalDateTime.now());
        return quizSessionRepository.save(session);
    }

    @Override
    @Scheduled(fixedRate = 300000) 
    public void cleanupInactiveSessions() {
        logger.debug("Running scheduled cleanup of inactive sessions");
        try {
            LocalDateTime timeoutThreshold = LocalDateTime.now().minusMinutes(sessionTimeoutMinutes);
            List<QuizSession> inactiveSessions = quizSessionRepository
                    .findByActiveAndLastAccessTimeBefore(true, timeoutThreshold);

            inactiveSessions.forEach(session -> {
                session.setActive(false);
                quizSessionRepository.save(session);
                logger.info("Deactivated expired session: {}", session.getId());
            });

            logger.info("Cleaned up {} inactive sessions", inactiveSessions.size());
        } catch (Exception e) {
            logger.error("Error during session cleanup", e);
        }
    }
}