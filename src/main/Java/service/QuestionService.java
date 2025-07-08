package service;

import dtos.question.QuestionCreateDto;
import repository.QuestionRepository;

public class QuestionService {
    private final QuestionRepository questionRepository;

    public QuestionService(QuestionRepository questionRepository) {
        this.questionRepository = questionRepository;
    }

    public void createQuestion(QuestionCreateDto dto) {
        questionRepository.create(dto);
    }
} 