package entities;
import java.io.Serializable;
import java.util.List;

public class QuestionBundle implements Serializable {
    private static final long serialVersionUID = 1L;
    private Question question;
    private QuestionAnswer answer;
    private List<Choice> choices;

    public QuestionBundle(Question question, QuestionAnswer answer, List<Choice> choices) {
        this.question = question;
        this.answer = answer;
        this.choices = choices;
    }

    public Question getQuestion() {
        return question;
    }

    public void setQuestion(Question question) {
        this.question = question;
    }

    public QuestionAnswer getAnswer() {
        return answer;
    }

    public void setAnswers(QuestionAnswer answer) {
        this.answer = answer;
    }

    public List<Choice> getChoices() {
        return choices;
    }

    public void setChoices(List<Choice> choices) {
        this.choices = choices;
    }
} 