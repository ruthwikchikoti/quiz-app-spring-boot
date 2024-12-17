package com.example.quizapp.service.interfaces;

import com.example.quizapp.dto.QuestionDTO;
import com.example.quizapp.model.Question;

public interface QuestionService {
    QuestionDTO getNextQuestion(Long sessionId);
    QuestionDTO mapQuestionToDTO(Question question);
}