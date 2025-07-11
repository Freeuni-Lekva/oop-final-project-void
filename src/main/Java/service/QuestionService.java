package service;

import entities.*;
import repository.*;

import java.util.List;

public class QuestionService {
    private final QuestionRepository questionRepo;
    private final QuestionAnswerRepository answerRepo;
    private final ChoiceRepository choiceRepo;

    public QuestionService(
            QuestionRepository questionRepo,
            QuestionAnswerRepository answerRepo,
            ChoiceRepository choiceRepo) {
        this.questionRepo = questionRepo;
        this.answerRepo = answerRepo;
        this.choiceRepo = choiceRepo;
    }

    public void createQuestionWithAnswersAndChoices(QuestionBundle bundle, int quizId) {
        Question question = bundle.getQuestion();
        question.setQuiz_id(quizId);
        questionRepo.create(question);

        int questionId = question.getQuestion_id();

        QuestionAnswer answer = bundle.getAnswer();
        if (answer != null) {
            answer.setQuestion_id(questionId);
            answerRepo.create(answer);
        }

        for (Choice choice : bundle.getChoices()) {
            choice.setQuestion_id(questionId);
            choiceRepo.create(choice);
        }
    }

    public void updateQuestionWithAnswersAndChoices(QuestionBundle bundle) throws Exception {
        Question question = bundle.getQuestion();
        questionRepo.update(question);

        int questionId = question.getQuestion_id();

        answerRepo.deleteById(questionId);
        choiceRepo.deleteById(questionId);

        QuestionAnswer answer = bundle.getAnswer();
        if (answer != null) {
            answer.setQuestion_id(questionId);
            answerRepo.create(answer);
        }

        for (Choice choice : bundle.getChoices()) {
            choice.setQuestion_id(questionId);
            choiceRepo.create(choice);
        }
    }

    public QuestionBundle getQuestionBundle(int questionId) throws Exception {
        Question question = questionRepo.getById(questionId);
        QuestionAnswer answer = answerRepo.findByQuestionId(questionId);
        List<Choice> choices = choiceRepo.findByQuestionId(questionId);

        return new QuestionBundle(question, answer, choices);
    }


    public void deleteQuestion(int questionId) throws Exception {
        answerRepo.deleteById(questionId);
        choiceRepo.deleteById(questionId);
        questionRepo.deleteById(questionId);
    }
}
