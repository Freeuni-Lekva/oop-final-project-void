package quiz.TakeQuiz.mappers;

import choice.Choice;
import question.Question;
import quiz.Quiz;
import quiz.TakeQuiz.dtos.TakeQuizDto;

import java.util.List;
import java.util.stream.Collectors;

public class QuizMapper {
    public static TakeQuizDto toDto(Quiz quiz, List<Question> questions, List<Choice> choices) {
        return TakeQuizDto.builder()
                .quizId(quiz.getQuizId())
                .title(quiz.getTitle())
                .description(quiz.getDescription())
                .creatorId(quiz.getCreatorId())
                .createdAt(quiz.getCreatedAt())
                .questions(questions.stream()
                        .map(q -> QuestionMapper.toDto(q, choices))
                        .collect(Collectors.toList()))
                .build();
    }
}
