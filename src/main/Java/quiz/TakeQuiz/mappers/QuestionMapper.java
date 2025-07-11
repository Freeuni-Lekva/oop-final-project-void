package quiz.TakeQuiz.mappers;

import choice.Choice;
import question.Question;
import quiz.TakeQuiz.dtos.TakeQuizQuestionDto;

import java.util.List;
import java.util.stream.Collectors;

public class QuestionMapper {
    public static TakeQuizQuestionDto toDto(Question question, List<Choice> allChoices) {
        return TakeQuizQuestionDto.builder()
                .question_id(question.getQuestionId())
                .question_text(question.getQuestionText())
                .type(question.getType())
                .image_url(question.getImageUrl())
                .choices(allChoices.stream()
                        .filter(c -> c.getQuestion_id() == question.getQuestionId())
                        .map(ChoiceMapper::toDto)
                        .collect(Collectors.toList()))
                .build();
    }
}
