package quiz.submitQuiz;

import choice.Choice;
import choice.ChoiceRepository;
import choice.ChoiceService;
import questionAnswer.QuestionAnswer;
import questionAnswer.QuestionAnswerService;
import quiz.submitQuiz.dtos.UserAnswerDto;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class QuizEvaluationService {

    private final QuestionAnswerService qaService;
    private final ChoiceService choiceService;

    public QuizEvaluationService(QuestionAnswerService qaService, ChoiceService choiceService) {
        this.qaService = qaService;
        this.choiceService = choiceService;
    }

    public int calculateScore(List<UserAnswerDto> userAnswers) {
        List<Integer> questionIds = userAnswers.stream()
                .map(a -> a.questionId)
                .collect(Collectors.toList());

        List<QuestionAnswer> correctAnswers = qaService.getCorrectAnswersByQuestionIds(questionIds);

        Map<Integer, String> correctMap = new HashMap<>();
        for(var q : correctAnswers) {
            correctMap.put(q.getQuestionId(), q.getAnswerText());
        }

        long correctCount = userAnswers.stream()
                .filter(userAnswer -> {
                    String correct = correctMap.get(userAnswer.questionId);
                    return correct != null && userAnswer.answerText != null && correct.equalsIgnoreCase(userAnswer.answerText.trim());
                })
                .count();

        List<Choice> correctChoices = choiceService.getCorrectChoices(questionIds);

        Map<Integer, String> correctChoiceMap = new HashMap<>();
        for (Choice choice : correctChoices) {
            correctChoiceMap.put(choice.getQuestion_id(), choice.getChoice_text());
        }

        long correctChoiceCount = userAnswers.stream()
                .filter(userAnswer -> {
                    String correct = correctChoiceMap.get(userAnswer.questionId);
                    return correct != null && userAnswer.answerText != null && correct.equalsIgnoreCase(userAnswer.answerText.trim());
                })
                .count();

        return (int)correctCount + (int)correctChoiceCount;
    }
}
