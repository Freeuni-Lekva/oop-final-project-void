package question;

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
public class Question implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;
    private Integer questionId;
    private Integer quizId;
    private String questionText;
    private String type;
    private String imageUrl;
    private Integer questionOrder;

    public Question() {

    }
}

