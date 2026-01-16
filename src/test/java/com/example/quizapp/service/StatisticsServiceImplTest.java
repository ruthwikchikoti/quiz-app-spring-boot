package com.example.quizapp.service;

import com.example.quizapp.dto.QuizDashboardDTO;
import com.example.quizapp.dto.QuizStatsDTO;
import com.example.quizapp.enums.Category;
import com.example.quizapp.enums.Difficulty;
import com.example.quizapp.model.Question;
import com.example.quizapp.model.QuizSession;
import com.example.quizapp.model.UserResponse;
import com.example.quizapp.repository.QuizSessionRepository;
import com.example.quizapp.repository.UserResponseRepository;
import com.example.quizapp.service.impl.StatisticsServiceImpl;
import com.example.quizapp.service.interfaces.SessionService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class StatisticsServiceImplTest {

    @Mock
    private UserResponseRepository userResponseRepository;

    @Mock
    private QuizSessionRepository quizSessionRepository;

    @Mock
    private SessionService sessionService;

    @InjectMocks
    private StatisticsServiceImpl statisticsService;

    private QuizSession quizSession;
    private Question question1;
    private Question question2;
    private UserResponse response1;
    private UserResponse response2;

    @BeforeEach
    void setUp() {
        quizSession = new QuizSession();
        quizSession.setId(1L);
        quizSession.setStartTime(LocalDateTime.now());
        quizSession.setLastAccessTime(LocalDateTime.now());
        quizSession.setActive(true);

        question1 = new Question();
        question1.setId(1L);
        question1.setCategory(Category.SCIENCE);
        question1.setDifficulty(Difficulty.EASY);
        question1.setTimeLimit(20);

        question2 = new Question();
        question2.setId(2L);
        question2.setCategory(Category.SCIENCE);
        question2.setDifficulty(Difficulty.MEDIUM);
        question2.setTimeLimit(30);

        response1 = new UserResponse();
        response1.setId(1L);
        response1.setQuizSession(quizSession);
        response1.setQuestion(question1);
        response1.setUserAnswer("B");
        response1.setCorrect(true);
        response1.setResponseTime(15);
        response1.setAnsweredAt(LocalDateTime.now());

        response2 = new UserResponse();
        response2.setId(2L);
        response2.setQuizSession(quizSession);
        response2.setQuestion(question2);
        response2.setUserAnswer("C");
        response2.setCorrect(false);
        response2.setResponseTime(25);
        response2.setAnsweredAt(LocalDateTime.now());
    }

    @Test
    void testGetSessionStats_Success() {
        List<UserResponse> responses = Arrays.asList(response1, response2);
        when(sessionService.validateAndUpdateSession(1L)).thenReturn(quizSession);
        when(userResponseRepository.findByQuizSession(quizSession)).thenReturn(responses);

        QuizStatsDTO result = statisticsService.getSessionStats(1L);

        assertNotNull(result);
        assertEquals(2, result.getTotalQuestions());
        assertEquals(1, result.getCorrectAnswers());
        assertEquals(1, result.getIncorrectAnswers());
        assertEquals(50.0, result.getAccuracyPercentage());
        verify(sessionService, times(1)).validateAndUpdateSession(1L);
        verify(userResponseRepository, times(1)).findByQuizSession(quizSession);
    }

    @Test
    void testGetSessionStats_NoResponses_ZeroAccuracy() {
        when(sessionService.validateAndUpdateSession(1L)).thenReturn(quizSession);
        when(userResponseRepository.findByQuizSession(quizSession)).thenReturn(Collections.emptyList());

        QuizStatsDTO result = statisticsService.getSessionStats(1L);

        assertNotNull(result);
        assertEquals(0, result.getTotalQuestions());
        assertEquals(0, result.getCorrectAnswers());
        assertEquals(0, result.getIncorrectAnswers());
    }

    @Test
    void testGetSessionStats_AllCorrect_100PercentAccuracy() {
        response2.setCorrect(true);
        List<UserResponse> responses = Arrays.asList(response1, response2);
        when(sessionService.validateAndUpdateSession(1L)).thenReturn(quizSession);
        when(userResponseRepository.findByQuizSession(quizSession)).thenReturn(responses);

        QuizStatsDTO result = statisticsService.getSessionStats(1L);

        assertNotNull(result);
        assertEquals(2, result.getTotalQuestions());
        assertEquals(2, result.getCorrectAnswers());
        assertEquals(100.0, result.getAccuracyPercentage());
    }

    @Test
    void testGetDashboard_Success() {
        List<UserResponse> responses = Arrays.asList(response1, response2);
        when(sessionService.validateAndUpdateSession(1L)).thenReturn(quizSession);
        when(userResponseRepository.findByQuizSession(quizSession)).thenReturn(responses);
        when(quizSessionRepository.count()).thenReturn(5L);

        QuizDashboardDTO result = statisticsService.getDashboard(1L);

        assertNotNull(result);
        assertEquals(5L, result.getTotalSessions());
        assertEquals(2, result.getQuestionsAttempted());
        assertNotNull(result.getCategoryAccuracy());
        assertNotNull(result.getBestCategory());
        assertNotNull(result.getBestDifficulty());
        verify(sessionService, times(1)).validateAndUpdateSession(1L);
        verify(userResponseRepository, times(1)).findByQuizSession(quizSession);
        verify(quizSessionRepository, times(1)).count();
    }

    @Test
    void testGetDashboard_EmptyResponses() {
        when(sessionService.validateAndUpdateSession(1L)).thenReturn(quizSession);
        when(userResponseRepository.findByQuizSession(quizSession)).thenReturn(Collections.emptyList());
        when(quizSessionRepository.count()).thenReturn(5L);

        QuizDashboardDTO result = statisticsService.getDashboard(1L);

        assertNotNull(result);
        assertEquals(5L, result.getTotalSessions());
        assertEquals(0, result.getQuestionsAttempted());
        assertEquals(0.0, result.getOverallAccuracy());
        assertNull(result.getBestCategory()); // When empty, bestCategory is null
    }

    @Test
    void testGetDashboard_RecentPerformance_GoodStart() {
        UserResponse response3 = new UserResponse();
        response3.setId(3L);
        response3.setQuizSession(quizSession);
        response3.setQuestion(question1);
        response3.setUserAnswer("B");
        response3.setCorrect(true);
        response3.setResponseTime(18);
        response3.setAnsweredAt(LocalDateTime.now());
        
        List<UserResponse> responses = Arrays.asList(response1, response2, response3);
        when(sessionService.validateAndUpdateSession(1L)).thenReturn(quizSession);
        when(userResponseRepository.findByQuizSession(quizSession)).thenReturn(responses);
        when(quizSessionRepository.count()).thenReturn(5L);

        QuizDashboardDTO result = statisticsService.getDashboard(1L);

        assertNotNull(result);
        assertNotNull(result.getRecentPerformance());
    }

    @Test
    void testGetDashboard_RecentPerformance_NotEnoughData() {
        when(sessionService.validateAndUpdateSession(1L)).thenReturn(quizSession);
        when(userResponseRepository.findByQuizSession(quizSession)).thenReturn(Collections.singletonList(response1));
        when(quizSessionRepository.count()).thenReturn(5L);

        QuizDashboardDTO result = statisticsService.getDashboard(1L);

        assertNotNull(result);
        assertEquals("Not enough data (need 3 questions)", result.getRecentPerformance());
    }
}
