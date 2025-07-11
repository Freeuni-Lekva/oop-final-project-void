package quiz;

import choice.ChoiceRepository;
import choice.Choice;
import org.apache.commons.dbcp2.BasicDataSource;
import question.Question;
import question.QuestionRepository;
import question.QuestionService;
import questionAnswer.QuestionAnswerRepository;
import questionBundle.QuestionBundle;
import quiz.TakeQuiz.dtos.TakeQuizDto;
import quiz.TakeQuiz.mappers.QuizMapper;

import java.util.List;

public class QuizService {
    private final QuizRepository quizRepo;
    private final QuestionRepository questionRepo;
    private final ChoiceRepository choiceRepo;
    private QuestionService questionService;

    public QuizService(BasicDataSource dataSource) {
        this.quizRepo = new QuizRepository(dataSource);
        this.questionRepo = new QuestionRepository(dataSource);
        this.choiceRepo = new ChoiceRepository(dataSource);
        questionService = new QuestionService(questionRepo, new QuestionAnswerRepository(dataSource), choiceRepo);
    }


    public TakeQuizDto getFullQuiz(int id) {
        Quiz quiz = quizRepo.getById(id);
        List<Question> questions  = questionRepo.getAllByQuizId(id);
        List<Choice> choices = choiceRepo.getAllChoicesByQuizId(id);
        TakeQuizDto dto = QuizMapper.toDto(quiz, questions, choices);
        return dto;
    }

    public void createQuizWithQuestions(Quiz quiz, List<QuestionBundle> bundles) throws Exception {
        quizRepo.create(quiz);
        //System.out.println("HEREEE" + quiz.getQuizId());
        int quizId = quiz.getQuizId();

        for (QuestionBundle bundle : bundles) {
            questionService.createQuestionWithAnswersAndChoices(bundle, quizId);
        }
    }
}
