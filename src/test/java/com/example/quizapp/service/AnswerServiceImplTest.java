package com.example.quizapp.service;

import com.example.quizapp.model.Question;
import com.example.quizapp.model.QuizSession;
import com.example.quizapp.model.UserResponse;
import com.example.quizapp.repository.QuestionRepository;
import com.example.quizapp.repository.QuizSessionRepository;
import com.example.quizapp.repository.UserResponseRepository;
import com.example.quizapp.service.impl.AnswerServiceImpl;
import com.example.quizapp.service.interfaces.SessionService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AnswerServiceImplTest {

    @Mock
    private QuestionRepository questionRepository;

    @Mock
    private UserResponseRepository userResponseRepository;

    @Mock
    private QuizSessionRepository quizSessionRepository;

    @Mock
    private SessionService sessionService;

    @InjectMocks
    private AnswerServiceImpl answerService;

    private QuizSession quizSession;
    private Question question;

    @BeforeEach
    void setUp() {
        quizSession = new QuizSession();
        quizSession.setId(1L);
        quizSession.setStartTime(LocalDateTime.now());
        quizSession.setLastAccessTime(LocalDateTime.now());
        quizSession.setActive(true);
        quizSession.setTotalQuestions(0);
        quizSession.setCorrectAnswers(0);
        quizSession.setCurrentQuestionId(1L);

        question = new Question();
        question.setId(1L);
        question.setQuestionText("What is 2+2?");
        question.setCorrectAnswer("B");
    }

    @Test
    void testSubmitAnswer_CorrectAnswer_ReturnsTrue() {
        when(sessionService.validateAndUpdateSession(1L)).thenReturn(quizSession);
        when(questionRepository.findById(1L)).thenReturn(Optional.of(question));
        when(quizSessionRepository.save(any(QuizSession.class))).thenReturn(quizSession);
        when(userResponseRepository.save(any(UserResponse.class))).thenReturn(new UserResponse());

        boolean result = answerService.submitAnswer(1L, "B");

        assertTrue(result);
        verify(sessionService, times(2)).validateAndUpdateSession(1L); // Called in submitAnswer and saveUserResponse
        verify(questionRepository, times(2)).findById(1L); // Called in submitAnswer and saveUserResponse
        verify(quizSessionRepository, times(1)).save(any(QuizSession.class));
        verify(userResponseRepository, times(1)).save(any(UserResponse.class));
    }

    @Test
    void testSubmitAnswer_IncorrectAnswer_ReturnsFalse() {
        when(sessionService.validateAndUpdateSession(1L)).thenReturn(quizSession);
        when(questionRepository.findById(1L)).thenReturn(Optional.of(question));
        when(quizSessionRepository.save(any(QuizSession.class))).thenReturn(quizSession);
        when(userResponseRepository.save(any(UserResponse.class))).thenReturn(new UserResponse());

        boolean result = answerService.submitAnswer(1L, "A");

        assertFalse(result);
        verify(sessionService, times(2)).validateAndUpdateSession(1L); // Called in submitAnswer and saveUserResponse
        verify(questionRepository, times(2)).findById(1L); // Called in submitAnswer and saveUserResponse
        verify(quizSessionRepository, times(1)).save(any(QuizSession.class));
        verify(userResponseRepository, times(1)).save(any(UserResponse.class));
    }

    @Test
    void testSubmitAnswer_CaseInsensitive_ReturnsTrue() {
        when(sessionService.validateAndUpdateSession(1L)).thenReturn(quizSession);
        when(questionRepository.findById(1L)).thenReturn(Optional.of(question));
        when(quizSessionRepository.save(any(QuizSession.class))).thenReturn(quizSession);
        when(userResponseRepository.save(any(UserResponse.class))).thenReturn(new UserResponse());

        boolean result = answerService.submitAnswer(1L, "b");

        assertTrue(result);
        verify(sessionService, times(2)).validateAndUpdateSession(1L);
    }

    @Test
    void testSubmitAnswer_NoCurrentQuestion_ThrowsException() {
        quizSession.setCurrentQuestionId(null);
        when(sessionService.validateAndUpdateSession(1L)).thenReturn(quizSession);

        assertThrows(RuntimeException.class, () -> answerService.submitAnswer(1L, "B"));
        verify(questionRepository, never()).findById(anyLong());
    }

    @Test
    void testSubmitAnswer_QuestionNotFound_ThrowsException() {
        when(sessionService.validateAndUpdateSession(1L)).thenReturn(quizSession);
        when(questionRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> answerService.submitAnswer(1L, "B"));
    }

    @Test
    void testSubmitAnswer_UpdatesSessionStatistics() {
        when(sessionService.validateAndUpdateSession(1L)).thenReturn(quizSession);
        when(questionRepository.findById(1L)).thenReturn(Optional.of(question));
        when(quizSessionRepository.save(any(QuizSession.class))).thenAnswer(invocation -> {
            QuizSession saved = invocation.getArgument(0);
            assertEquals(1, saved.getTotalQuestions());
            assertEquals(1, saved.getCorrectAnswers());
            return saved;
        });
        when(userResponseRepository.save(any(UserResponse.class))).thenReturn(new UserResponse());

        answerService.submitAnswer(1L, "B");

        verify(quizSessionRepository, times(1)).save(any(QuizSession.class));
    }

    @Test
    void testSaveUserResponse_Success() {
        when(sessionService.validateAndUpdateSession(1L)).thenReturn(quizSession);
        when(questionRepository.findById(1L)).thenReturn(Optional.of(question));
        when(userResponseRepository.save(any(UserResponse.class))).thenAnswer(invocation -> {
            UserResponse response = invocation.getArgument(0);
            response.setId(1L);
            return response;
        });

        UserResponse result = answerService.saveUserResponse(1L, "B", true);

        assertNotNull(result);
        verify(userResponseRepository, times(1)).save(any(UserResponse.class));
    }
}
