package com.example.quizapp.service.impl;

import com.example.quizapp.dto.QuestionDTO;
import com.example.quizapp.exception.QuizException;
import com.example.quizapp.model.Question;
import com.example.quizapp.model.QuizSession;
import com.example.quizapp.repository.QuestionRepository;
import com.example.quizapp.service.interfaces.QuestionService;
import com.example.quizapp.service.interfaces.SessionService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class QuestionServiceImpl implements QuestionService {
    private static final Logger logger = LoggerFactory.getLogger(QuestionServiceImpl.class);

    private final QuestionRepository questionRepository;
    private final SessionService sessionService;

    @Override
    @Transactional
    public QuestionDTO getNextQuestion(Long sessionId) {
        logger.debug("Fetching next question for session: {}", sessionId);
        QuizSession session = sessionService.validateAndUpdateSession(sessionId);

        Question question = questionRepository.findRandomQuestion();
        if (question == null) {
            throw new QuizException("No questions available");
        }

        // Store the current question ID in the session
        session.setCurrentQuestionId(question.getId());
        sessionService.saveSession(session);

        QuestionDTO dto = mapQuestionToDTO(question);
        dto.setTimestamp(System.currentTimeMillis());

        logger.info("Question retrieved: Category={}, Difficulty={}",
                question.getCategory(), question.getDifficulty());
        return dto;
    }

    @Override
    public QuestionDTO mapQuestionToDTO(Question question) {
        QuestionDTO dto = new QuestionDTO();
        dto.setQuestionText(question.getQuestionText());
        dto.setOptionA(question.getOptionA());
        dto.setOptionB(question.getOptionB());
        dto.setOptionC(question.getOptionC());
        dto.setOptionD(question.getOptionD());
        dto.setCategory(question.getCategory());
        dto.setDifficulty(question.getDifficulty());
        dto.setTimeLimit(question.getTimeLimit());
        return dto;
    }
}
