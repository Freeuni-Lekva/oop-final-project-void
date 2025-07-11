package questionBundle;
import choice.Choice;
import lombok.Getter;
import lombok.Setter;
import question.Question;
import questionAnswer.QuestionAnswer;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

@Getter
public class QuestionBundle implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;
    @Setter
    private Question question;
    private QuestionAnswer answer;
    @Setter
    private List<Choice> choices;

    public QuestionBundle(Question question, QuestionAnswer answer, List<Choice> choices) {
        this.question = question;
        this.answer = answer;
        this.choices = choices;
    }

    public QuestionBundle() {

    }

    public void setAnswers(QuestionAnswer answer) {
        this.answer = answer;
    }

}