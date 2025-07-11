package quiz.createQuizFinish;

import questionBundle.QuestionBundle;
import quiz.Quiz;
import quiz.QuizService;
import org.apache.commons.dbcp2.BasicDataSource;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.util.List;

@WebServlet("/finishQuiz")
public class FinishQuizServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Quiz quiz = (Quiz) request.getSession().getAttribute("quizEntity");
        List<QuestionBundle> questionBundles = (List<QuestionBundle>) request.getSession().getAttribute("questionBundles");

        if (quiz == null || questionBundles == null || questionBundles.isEmpty()) {
            request.setAttribute("errorMessage", "You must add at least one question before finishing your quiz.");
            request.getRequestDispatcher("/addQuestion.jsp").forward(request, response);
            return;
        }

        try {
            BasicDataSource ds = resources.DatabaseConnection.getDataSource();
            QuizService quizService = new QuizService(ds);

            quizService.createQuizWithQuestions(quiz, questionBundles);
            request.getSession().removeAttribute("quizEntity");
            request.getSession().removeAttribute("questionBundles");
            request.getSession().removeAttribute("tempQuizId");
            request.getRequestDispatcher("/quizSuccess.jsp").forward(request, response);

        } catch (Exception e) {
            e.printStackTrace();
            response.getWriter().write("Failed to finish quiz: " + e.getMessage());
        }
    }
}
