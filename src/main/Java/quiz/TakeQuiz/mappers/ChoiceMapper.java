package quiz.TakeQuiz.mappers;

import choice.Choice;
import quiz.TakeQuiz.dtos.TakeQuizChoiceDto;

public class ChoiceMapper {
    public static TakeQuizChoiceDto toDto(Choice choice) {
        return TakeQuizChoiceDto.builder()
                .choice_text(choice.getChoice_text())
                .is_correct(choice.getIs_correct())
                .build();
    }
}
