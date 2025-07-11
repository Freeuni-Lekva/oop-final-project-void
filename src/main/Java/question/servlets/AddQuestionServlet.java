package question.servlets;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;

import java.io.IOException;
import java.util.*;

import choice.Choice;
import question.Question;
import questionAnswer.QuestionAnswer;
import questionBundle.QuestionBundle;

@WebServlet("/addQuestion")
public class AddQuestionServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        List<QuestionBundle> questionBundles = getOrCreateQuestionBundles(request);
        String tempQuizId = request.getParameter("quizId");
        int questionOrder = parseIntOrDefault(request.getParameter("question_order"));
        String questionType = request.getParameter("questionType");

        System.out.println(questionType);
        String errorMessage;
        QuestionBundle bundle = buildQuestionBundle(request, questionType, questionOrder);

        if (!isValid(bundle)) {
            System.out.println("Not a valid answer");
            errorMessage = "You must fill in every required field.";
            request.setAttribute("errorMessage", errorMessage);
            request.setAttribute("quizId", tempQuizId);
            request.setAttribute("nextOrder", questionOrder);
            request.getRequestDispatcher("/addQuestion.jsp").forward(request, response);
            return;
        }

        questionBundles.add(bundle);
        response.sendRedirect("addQuestion.jsp?quizId=" + tempQuizId + "&nextOrder=" + (questionOrder + 1));
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.getRequestDispatcher("/addQuestion.jsp").forward(request, response);
    }

    // -------------------------------------------------------------------------------------------------------

    private List<QuestionBundle> getOrCreateQuestionBundles(HttpServletRequest request) {
        List<QuestionBundle> questionBundles = (List<QuestionBundle>) request.getSession().getAttribute("questionBundles");
        if (questionBundles == null) {
            questionBundles = new ArrayList<>();
            request.getSession().setAttribute("questionBundles", questionBundles);
        }
        return questionBundles;
    }

    private int parseIntOrDefault(String value) {
        try {
            return Integer.parseInt(value);
        } catch (Exception e) {
            return 1;
        }
    }

    private QuestionBundle buildQuestionBundle(HttpServletRequest request, String questionType, int questionOrder) {
        String questionText = null, imageUrl = null;
        QuestionAnswer answer = null;
        List<Choice> choices = new ArrayList<>();
        String answerText;

        switch (questionType) {
            case "question_response":
                questionText = request.getParameter("qr-question");
                answerText = request.getParameter("qr-answer");
                if (answerText != null && !answerText.trim().isEmpty()) {
                    answer = new QuestionAnswer(null, null, answerText.trim());
                }
                break;

            case "fill_blank":
                questionText = request.getParameter("fb-question");
                answerText = request.getParameter("fb-answer");
                if (answerText != null && !answerText.trim().isEmpty()) {
                    answer = new QuestionAnswer(null, null, answerText.trim());
                }
                break;

            case "multiple_choice":
                questionText = request.getParameter("mc-question");
                handleMultipleChoice(request, choices);
                break;

            case "picture_response":
                questionText = request.getParameter("pr-question");
                imageUrl = request.getParameter("pr-imageUrl");
                answerText = request.getParameter("pr-answer");
                if (answerText != null && !answerText.trim().isEmpty()) {
                    answer = new QuestionAnswer(null, null, answerText.trim());
                }
                break;
        }

        Question question = new Question(null, null, questionText, questionType, imageUrl, questionOrder);
        return new QuestionBundle(question, answer, choices);
    }


    private void handleMultipleChoice(HttpServletRequest request, List<Choice> choices) {
        String[] choiceTexts = {
                request.getParameter("mc-choice1"),
                request.getParameter("mc-choice2"),
                request.getParameter("mc-choice3"),
                request.getParameter("mc-choice4")
        };
        int correctIndex = parseIntOrDefault(request.getParameter("mc-correct"));

        for (int i = 0; i < choiceTexts.length; i++) {
            String text = choiceTexts[i];
            if (text != null && !text.trim().isEmpty()) {
                boolean isCorrect = (i + 1) == correctIndex;
                choices.add(new Choice(null, null, text.trim(), isCorrect));
            }
        }
    }

    private boolean isValid(QuestionBundle bundle) {
        Question q = bundle.getQuestion();
        if (q.getQuestionText() == null || q.getQuestionText().trim().isEmpty())
            return false;

        return switch (q.getType()) {
            case "question_response", "fill_blank", "picture_response" -> {
                QuestionAnswer answer = bundle.getAnswer();
                yield answer != null && !answer.getAnswerText().trim().isEmpty();
            }
            case "multiple_choice" -> {
                boolean hasCorrect = bundle.getChoices().stream().anyMatch(Choice::getIsCorrect);
                yield bundle.getChoices().size() >= 2 && hasCorrect;
            }
            default -> false;
        };
    }

}

