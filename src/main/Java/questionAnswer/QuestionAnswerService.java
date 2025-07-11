package questionAnswer;
import java.util.List;

public class QuestionAnswerService {

    private final QuestionAnswerRepository repository;

    public QuestionAnswerService(QuestionAnswerRepository repository) {
        this.repository = repository;
    }

    public List<QuestionAnswer> getCorrectAnswersByQuestionIds(List<Integer> questionIds) {
        return repository.getCorrectAnswersByQuestionIds(questionIds);
    }

}

