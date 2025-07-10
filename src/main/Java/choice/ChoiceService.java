package choice;

import questionAnswer.QuestionAnswer;
import recources.DatabaseConnection;

import java.util.ArrayList;
import java.util.List;

public class ChoiceService {

    private final ChoiceRepository choiceRepository;

    public ChoiceService(ChoiceRepository choiceRepository) {
        this.choiceRepository = choiceRepository;
    }

    public List<Choice> getChoicesByQuestionId(int questionId) {
        if (questionId <= 0) {
            throw new IllegalArgumentException("Invalid question ID");
        }

        return choiceRepository.getAllChoicesByQuestionId(questionId);
    }

    public List<Choice> getChoicesByQuizId(int quizId) {
        if (quizId <= 0) {
            throw new IllegalArgumentException("Invalid quiz ID");
        }

        return choiceRepository.getAllChoicesByQuizId(quizId);
    }

    public List<Choice> getCorrectChoices(List<Integer> questionIds) {
        List<Choice> correctChoices = new ArrayList<Choice>();
        ChoiceRepository choiceRepository = new ChoiceRepository(DatabaseConnection.getDataSource());
        for(int i : questionIds) {
            List<Choice> choices = choiceRepository.getAllChoicesByQuestionId(i);
            Choice correctChoice = choices.stream()
                    .filter(Choice::getIs_correct)
                    .findFirst()
                    .orElse(null); // or throw exception if not found
            if(correctChoice == null) continue;
            correctChoices.add( Choice.builder().choice_id(correctChoice.getChoice_id())
                            .choice_text(correctChoice.getChoice_text())
                            .is_correct(correctChoice.getIs_correct())
                            .question_id(correctChoice.getQuestion_id())
                            .choice_id(correctChoice.getChoice_id()).build());

        }
        return correctChoices;
    }
}

