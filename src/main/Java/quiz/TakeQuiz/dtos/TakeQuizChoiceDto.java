package quiz.TakeQuiz.dtos;


import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class TakeQuizChoiceDto {
    private String choice_text;
    private Boolean is_correct;
}
