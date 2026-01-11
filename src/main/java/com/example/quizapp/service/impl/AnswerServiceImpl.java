package com.example.quizapp.service.impl;

import com.example.quizapp.model.Question;
import com.example.quizapp.model.QuizSession;
import com.example.quizapp.model.UserResponse;
import com.example.quizapp.repository.QuestionRepository;
import com.example.quizapp.repository.QuizSessionRepository;
import com.example.quizapp.repository.UserResponseRepository;
import com.example.quizapp.service.interfaces.AnswerService;
import com.example.quizapp.service.interfaces.SessionService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class AnswerServiceImpl implements AnswerService {
    private static final Logger logger = LoggerFactory.getLogger(AnswerServiceImpl.class);

    private final QuestionRepository questionRepository;
    private final UserResponseRepository userResponseRepository;
    private final QuizSessionRepository quizSessionRepository;
    private final SessionService sessionService;

    @Override
    @Transactional
    public boolean submitAnswer(Long sessionId, String answer) {
        QuizSession session = sessionService.validateAndUpdateSession(sessionId);

        if (session.getCurrentQuestionId() == null) {
            throw new RuntimeException("No current question set for this session");
        }

        Question currentQuestion = questionRepository.findById(session.getCurrentQuestionId())
                .orElseThrow(() -> new RuntimeException("Question not found"));

        boolean isCorrect = currentQuestion.getCorrectAnswer().equalsIgnoreCase(answer);

        // Update session statistics
        session.setTotalQuestions(session.getTotalQuestions() + 1);
        if (isCorrect) {
            session.setCorrectAnswers(session.getCorrectAnswers() + 1);
        }
        quizSessionRepository.save(session);

        // Save user response
        saveUserResponse(sessionId, answer, isCorrect);

        logger.info("Answer submitted for session {}: correct={}, total={}, correct={}",
                sessionId, isCorrect, session.getTotalQuestions(), session.getCorrectAnswers());

        return isCorrect;
    }

    @Override
    public UserResponse saveUserResponse(Long sessionId, String answer, boolean isCorrect) {
        QuizSession session = sessionService.validateAndUpdateSession(sessionId);

        if (session.getCurrentQuestionId() == null) {
            throw new RuntimeException("No current question set for this session");
        }

        Question currentQuestion = questionRepository.findById(session.getCurrentQuestionId())
                .orElseThrow(() -> new RuntimeException("Question not found"));

        UserResponse response = new UserResponse();
        response.setQuizSession(session);
        response.setQuestion(currentQuestion);
        response.setUserAnswer(answer);
        response.setCorrect(isCorrect);
        response.setAnsweredAt(LocalDateTime.now());
        response.setResponseTime(20); 

        return userResponseRepository.save(response);
    }
}