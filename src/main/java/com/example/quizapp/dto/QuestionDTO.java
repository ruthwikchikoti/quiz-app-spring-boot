package com.example.quizapp.dto;

import com.example.quizapp.enums.Category;
import com.example.quizapp.enums.Difficulty;
import lombok.Data;

@Data
public class QuestionDTO {
    private String questionText;
    private String optionA;
    private String optionB;
    private String optionC;
    private String optionD;
    private Category category;
    private Difficulty difficulty;
    private Integer timeLimit;
    private Long timestamp;
}
