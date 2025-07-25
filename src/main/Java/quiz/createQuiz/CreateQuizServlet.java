package quiz.createQuiz;

import loginflow.UsersRepository;
import question.Question;
import quiz.Quiz;
import resources.DatabaseConnection;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Timestamp;

@WebServlet("/createQuiz")
public class CreateQuizServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String title = request.getParameter("title");
        String description = request.getParameter("description");
        String username = (String) request.getSession().getAttribute("username");
        int creatorId = new UsersRepository(DatabaseConnection.getDataSource()).findByName(username).getUser_id();


        Quiz quiz = new Quiz(null, title, description, creatorId, new Timestamp(System.currentTimeMillis()));
        request.getSession().setAttribute("quizEntity", quiz);

        //randomizing the quiz id for session tracking only
        String tempQuizId = java.util.UUID.randomUUID().toString();
        request.getSession().setAttribute("tempQuizId", tempQuizId);

        request.getSession().setAttribute("questionEntities", new java.util.ArrayList<Question>());
        response.sendRedirect("addQuestion.jsp?quizId=" + tempQuizId);
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
//        if (request.getSession().getAttribute("userId") == null) {
//            request.getSession().setAttribute("userId", 1L); //simulates for now
//        }
        request.getRequestDispatcher("/createQuiz.jsp").forward(request, response);
    }
}
