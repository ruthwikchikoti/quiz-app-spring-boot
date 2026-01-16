package com.example.quizapp.service;

import com.example.quizapp.dto.QuestionDTO;
import com.example.quizapp.enums.Category;
import com.example.quizapp.enums.Difficulty;
import com.example.quizapp.exception.QuizException;
import com.example.quizapp.model.Question;
import com.example.quizapp.model.QuizSession;
import com.example.quizapp.repository.QuestionRepository;
import com.example.quizapp.service.impl.QuestionServiceImpl;
import com.example.quizapp.service.interfaces.SessionService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class QuestionServiceImplTest {

    @Mock
    private QuestionRepository questionRepository;

    @Mock
    private SessionService sessionService;

    @InjectMocks
    private QuestionServiceImpl questionService;

    private Question question;
    private QuizSession quizSession;

    @BeforeEach
    void setUp() {
        question = new Question();
        question.setId(1L);
        question.setQuestionText("What is 2+2?");
        question.setOptionA("3");
        question.setOptionB("4");
        question.setOptionC("5");
        question.setOptionD("6");
        question.setCorrectAnswer("B");
        question.setCategory(Category.SCIENCE);
        question.setDifficulty(Difficulty.EASY);
        question.setTimeLimit(20);

        quizSession = new QuizSession();
        quizSession.setId(1L);
        quizSession.setStartTime(LocalDateTime.now());
        quizSession.setLastAccessTime(LocalDateTime.now());
        quizSession.setActive(true);
    }

    @Test
    void testGetNextQuestion_Success() {
        when(sessionService.validateAndUpdateSession(1L)).thenReturn(quizSession);
        when(questionRepository.findRandomQuestion()).thenReturn(question);
        doNothing().when(sessionService).saveSession(any(QuizSession.class));

        QuestionDTO result = questionService.getNextQuestion(1L);

        assertNotNull(result);
        assertEquals("What is 2+2?", result.getQuestionText());
        assertEquals("3", result.getOptionA());
        assertEquals("4", result.getOptionB());
        assertEquals("5", result.getOptionC());
        assertEquals("6", result.getOptionD());
        assertEquals(Category.SCIENCE, result.getCategory());
        assertEquals(Difficulty.EASY, result.getDifficulty());
        assertEquals(20, result.getTimeLimit());
        assertNotNull(result.getTimestamp());
        
        verify(sessionService, times(1)).validateAndUpdateSession(1L);
        verify(questionRepository, times(1)).findRandomQuestion();
        verify(sessionService, times(1)).saveSession(any(QuizSession.class));
    }

    @Test
    void testGetNextQuestion_NoQuestionsAvailable_ThrowsException() {
        when(sessionService.validateAndUpdateSession(1L)).thenReturn(quizSession);
        when(questionRepository.findRandomQuestion()).thenReturn(null);

        assertThrows(QuizException.class, () -> questionService.getNextQuestion(1L));
        verify(questionRepository, times(1)).findRandomQuestion();
        verify(sessionService, never()).saveSession(any(QuizSession.class));
    }

    @Test
    void testMapQuestionToDTO_Success() {
        QuestionDTO result = questionService.mapQuestionToDTO(question);

        assertNotNull(result);
        assertEquals("What is 2+2?", result.getQuestionText());
        assertEquals("3", result.getOptionA());
        assertEquals("4", result.getOptionB());
        assertEquals("5", result.getOptionC());
        assertEquals("6", result.getOptionD());
        assertEquals(Category.SCIENCE, result.getCategory());
        assertEquals(Difficulty.EASY, result.getDifficulty());
        assertEquals(20, result.getTimeLimit());
    }
}
