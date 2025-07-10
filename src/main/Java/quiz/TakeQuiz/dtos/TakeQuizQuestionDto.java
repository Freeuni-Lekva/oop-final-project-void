package quiz.TakeQuiz.dtos;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Builder
@Getter
public class TakeQuizQuestionDto {
    private Integer question_id;
    private Integer quiz_id;
    private String question_text;
    private String type;
    private String image_url;
    private Integer question_order;
    private List<TakeQuizChoiceDto> choices;
}
