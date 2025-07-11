package questionAnswer;


import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter
@Setter
public class QuestionAnswer {
    private Integer answerId;
    private Integer questionId;
    private String answerText;
}
