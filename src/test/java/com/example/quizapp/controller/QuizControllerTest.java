package com.example.quizapp.controller;

import com.example.quizapp.dto.*;
import com.example.quizapp.exception.QuizException;
import com.example.quizapp.service.interfaces.AnswerService;
import com.example.quizapp.service.interfaces.QuestionService;
import com.example.quizapp.service.interfaces.SessionService;
import com.example.quizapp.service.interfaces.StatisticsService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class QuizControllerTest {

    @Mock
    private SessionService sessionService;

    @Mock
    private QuestionService questionService;

    @Mock
    private AnswerService answerService;

    @Mock
    private StatisticsService statisticsService;

    @InjectMocks
    private QuizController quizController;

    private QuizSessionDTO quizSessionDTO;
    private QuestionDTO questionDTO;
    private QuizStatsDTO quizStatsDTO;
    private QuizDashboardDTO quizDashboardDTO;
    private AnswerSubmissionDTO answerSubmissionDTO;

    @BeforeEach
    void setUp() {
        quizSessionDTO = new QuizSessionDTO();
        quizSessionDTO.setSessionId(1L);
        quizSessionDTO.setMessage("New quiz session started successfully!");

        questionDTO = new QuestionDTO();
        questionDTO.setQuestionText("What is 2+2?");
        questionDTO.setOptionA("3");
        questionDTO.setOptionB("4");
        questionDTO.setOptionC("5");
        questionDTO.setOptionD("6");

        quizStatsDTO = new QuizStatsDTO();
        quizStatsDTO.setTotalQuestions(10);
        quizStatsDTO.setCorrectAnswers(7);
        quizStatsDTO.setIncorrectAnswers(3);
        quizStatsDTO.setAccuracyPercentage(70.0);

        quizDashboardDTO = new QuizDashboardDTO();
        quizDashboardDTO.setTotalSessions(5L);
        quizDashboardDTO.setQuestionsAttempted(50);

        answerSubmissionDTO = new AnswerSubmissionDTO();
        answerSubmissionDTO.setSessionId(1L);
        answerSubmissionDTO.setAnswer("B");
    }

    @Test
    void testStartNewSession_Success() {
        when(sessionService.startNewSession()).thenReturn(quizSessionDTO);

        ResponseEntity<QuizSessionDTO> response = quizController.startNewSession();

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(1L, response.getBody().getSessionId());
        assertEquals("New quiz session started successfully!", response.getBody().getMessage());
        verify(sessionService, times(1)).startNewSession();
    }

    @Test
    void testGetQuestion_Success() {
        when(questionService.getNextQuestion(1L)).thenReturn(questionDTO);

        ResponseEntity<QuestionDTO> response = quizController.getQuestion(1L);

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("What is 2+2?", response.getBody().getQuestionText());
        verify(questionService, times(1)).getNextQuestion(1L);
    }

    @Test
    void testGetQuestion_NullSessionId_ThrowsException() {
        assertThrows(QuizException.class, () -> quizController.getQuestion(null));
        verify(questionService, never()).getNextQuestion(anyLong());
    }

    @Test
    void testSubmitAnswer_Success() {
        when(answerService.submitAnswer(1L, "B")).thenReturn(true);

        ResponseEntity<Boolean> response = quizController.submitAnswer(answerSubmissionDTO);

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue(response.getBody());
        verify(answerService, times(1)).submitAnswer(1L, "B");
    }

    @Test
    void testSubmitAnswer_NullSessionId_ThrowsException() {
        answerSubmissionDTO.setSessionId(null);
        assertThrows(QuizException.class, () -> quizController.submitAnswer(answerSubmissionDTO));
        verify(answerService, never()).submitAnswer(anyLong(), anyString());
    }

    @Test
    void testSubmitAnswer_NullAnswer_ThrowsException() {
        answerSubmissionDTO.setAnswer(null);
        assertThrows(QuizException.class, () -> quizController.submitAnswer(answerSubmissionDTO));
        verify(answerService, never()).submitAnswer(anyLong(), anyString());
    }

    @Test
    void testGetStats_Success() {
        when(statisticsService.getSessionStats(1L)).thenReturn(quizStatsDTO);

        ResponseEntity<QuizStatsDTO> response = quizController.getStats(1L);

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(10, response.getBody().getTotalQuestions());
        assertEquals(7, response.getBody().getCorrectAnswers());
        assertEquals(70.0, response.getBody().getAccuracyPercentage());
        verify(statisticsService, times(1)).getSessionStats(1L);
    }

    @Test
    void testGetStats_NullSessionId_ThrowsException() {
        assertThrows(QuizException.class, () -> quizController.getStats(null));
        verify(statisticsService, never()).getSessionStats(anyLong());
    }

    @Test
    void testGetDashboard_Success() {
        when(statisticsService.getDashboard(1L)).thenReturn(quizDashboardDTO);

        ResponseEntity<QuizDashboardDTO> response = quizController.getDashboard(1L);

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(5L, response.getBody().getTotalSessions());
        assertEquals(50, response.getBody().getQuestionsAttempted());
        verify(statisticsService, times(1)).getDashboard(1L);
    }

    @Test
    void testGetDashboard_NullSessionId_ThrowsException() {
        assertThrows(QuizException.class, () -> quizController.getDashboard(null));
        verify(statisticsService, never()).getDashboard(anyLong());
    }
}
