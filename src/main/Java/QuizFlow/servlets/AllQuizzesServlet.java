package QuizFlow.servlets;

import entities.Quiz;
import org.apache.commons.dbcp2.BasicDataSource;
import repository.QuizRepository;
import repository.UserRepository;
import resources.DatabaseConnection;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.*;

@WebServlet("/allQuizzes")
public class AllQuizzesServlet extends HttpServlet {
    private QuizRepository quizRepository;
    private UserRepository userRepository;

    @Override
    public void init() {
        BasicDataSource dataSource = DatabaseConnection.getDataSource();
        quizRepository = new QuizRepository(dataSource);
        userRepository = new UserRepository(dataSource);
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        try {
            List<Quiz> quizzes = quizRepository.getAll();
            Map<Long, String> userIdToUsername = new HashMap<>();

            for (Quiz quiz : quizzes) {
                Long creatorId = (long) quiz.getCreator_id();
                if (!userIdToUsername.containsKey(creatorId)) {
                    String username = userRepository.getUsernameById(creatorId);
                    if (username != null) {
                        userIdToUsername.put(creatorId, username);
                    }
                }
            }

            request.setAttribute("quizzes", quizzes);
            request.setAttribute("userIdToUsername", userIdToUsername);
            request.getRequestDispatcher("/allQuizzes.jsp").forward(request, response);
        } catch (Exception e) {
            e.printStackTrace();
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Error loading quizzes.");
        }
    }
}
