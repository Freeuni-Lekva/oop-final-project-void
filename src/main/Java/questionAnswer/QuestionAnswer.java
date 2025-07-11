package questionAnswer;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.io.Serial;
import java.io.Serializable;

@Builder
@Getter
@Setter
@AllArgsConstructor
public class QuestionAnswer implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;
    private Integer answerId;
    private Integer questionId;
    private String answerText;

    public QuestionAnswer() {

    }
}
