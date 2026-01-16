package com.example.quizapp.service;

import com.example.quizapp.dto.QuizSessionDTO;
import com.example.quizapp.exception.QuizException;
import com.example.quizapp.model.QuizSession;
import com.example.quizapp.repository.QuizSessionRepository;
import com.example.quizapp.service.impl.SessionServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SessionServiceImplTest {

    @Mock
    private QuizSessionRepository quizSessionRepository;

    @InjectMocks
    private SessionServiceImpl sessionService;

    private QuizSession quizSession;

    @BeforeEach
    void setUp() {
        ReflectionTestUtils.setField(sessionService, "sessionTimeoutMinutes", 30);
        
        quizSession = new QuizSession();
        quizSession.setId(1L);
        quizSession.setStartTime(LocalDateTime.now());
        quizSession.setLastAccessTime(LocalDateTime.now());
        quizSession.setActive(true);
        quizSession.setTotalQuestions(0);
        quizSession.setCorrectAnswers(0);
    }

    @Test
    void testStartNewSession_Success() {
        when(quizSessionRepository.save(any(QuizSession.class))).thenReturn(quizSession);

        QuizSessionDTO result = sessionService.startNewSession();

        assertNotNull(result);
        assertEquals(1L, result.getSessionId());
        assertEquals("New quiz session started successfully!", result.getMessage());
        verify(quizSessionRepository, times(1)).save(any(QuizSession.class));
    }

    @Test
    void testStartNewSession_ExceptionThrown() {
        when(quizSessionRepository.save(any(QuizSession.class)))
                .thenThrow(new RuntimeException("Database error"));

        assertThrows(QuizException.class, () -> sessionService.startNewSession());
    }

    @Test
    void testValidateAndUpdateSession_Success() {
        when(quizSessionRepository.findById(1L)).thenReturn(Optional.of(quizSession));
        when(quizSessionRepository.save(any(QuizSession.class))).thenReturn(quizSession);

        QuizSession result = sessionService.validateAndUpdateSession(1L);

        assertNotNull(result);
        assertTrue(result.isActive());
        verify(quizSessionRepository, times(1)).findById(1L);
        verify(quizSessionRepository, times(1)).save(any(QuizSession.class));
    }

    @Test
    void testValidateAndUpdateSession_NullSessionId_ThrowsException() {
        assertThrows(QuizException.class, () -> sessionService.validateAndUpdateSession(null));
        verify(quizSessionRepository, never()).findById(anyLong());
    }

    @Test
    void testValidateAndUpdateSession_InvalidSessionId_ThrowsException() {
        when(quizSessionRepository.findById(999L)).thenReturn(Optional.empty());

        assertThrows(QuizException.class, () -> sessionService.validateAndUpdateSession(999L));
        verify(quizSessionRepository, times(1)).findById(999L);
    }

    @Test
    void testValidateAndUpdateSession_InactiveSession_ThrowsException() {
        quizSession.setActive(false);
        when(quizSessionRepository.findById(1L)).thenReturn(Optional.of(quizSession));

        assertThrows(QuizException.class, () -> sessionService.validateAndUpdateSession(1L));
        verify(quizSessionRepository, times(1)).findById(1L);
    }

    @Test
    void testValidateAndUpdateSession_ExpiredSession_ThrowsException() {
        quizSession.setLastAccessTime(LocalDateTime.now().minusMinutes(31));
        when(quizSessionRepository.findById(1L)).thenReturn(Optional.of(quizSession));
        when(quizSessionRepository.save(any(QuizSession.class))).thenReturn(quizSession);

        assertThrows(QuizException.class, () -> sessionService.validateAndUpdateSession(1L));
        verify(quizSessionRepository, times(1)).findById(1L);
        verify(quizSessionRepository, times(1)).save(any(QuizSession.class));
    }

    @Test
    void testSaveSession_Success() {
        sessionService.saveSession(quizSession);
        verify(quizSessionRepository, times(1)).save(quizSession);
    }
}
