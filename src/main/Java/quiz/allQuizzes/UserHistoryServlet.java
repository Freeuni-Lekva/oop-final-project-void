package quiz.allQuizzes;

import loginflow.UsersRepository;
import org.apache.commons.dbcp2.BasicDataSource;
import quiz.Quiz;
import quiz.QuizRepository;
import quizAttempt.QuizAttempt;
import quizAttempt.QuizAttemptRepository;
import repository.UserRepository;
import resources.DatabaseConnection;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.*;

@WebServlet("/userHistory")
public class UserHistoryServlet extends HttpServlet {
    private QuizRepository quizRepository;
    private QuizAttemptRepository quizAttemptRepository;
    private UserRepository userRepository;
    @Override
    public void init() {
        BasicDataSource dataSource = DatabaseConnection.getDataSource();
        quizRepository = new QuizRepository(dataSource);
        quizAttemptRepository = new QuizAttemptRepository(dataSource);
        userRepository = new UserRepository(dataSource);
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("userId") == null) {
            response.sendRedirect("login.jsp");
            return;
        }

        try {
            String username = (String) request.getSession().getAttribute("username");
            int userId = new UsersRepository(DatabaseConnection.getDataSource()).findByName(username).getUser_id();
            //long userId = (long) request.getSession().getAttribute("userId");
            List<QuizAttempt> userAttempts = quizAttemptRepository.getAllAttemptsByUser((long) userId);
            Map<Long, Quiz> quizDetails = new HashMap<>();
            Map<Long, String> userIdToUsername = new HashMap<>();
            
            for (QuizAttempt attempt : userAttempts) {
                Long quizId = Long.valueOf(attempt.getQuizId());
                Long creatorId = Long.valueOf(attempt.getUserId());

                if (!quizDetails.containsKey(quizId)) {
                    Quiz quiz = quizRepository.getById(quizId);
                    if (quiz != null) quizDetails.put(quizId, quiz);
                }

                if (!userIdToUsername.containsKey(creatorId)) {
                    //String username = userRepository.getUsernameById(creatorId);
                    if (username != null) userIdToUsername.put(creatorId, username);
                }
            }

            request.setAttribute("userAttempts", userAttempts);
            request.setAttribute("quizDetails", quizDetails);
            request.setAttribute("userIdToUsername", userIdToUsername);
            request.getRequestDispatcher("/userHistory.jsp").forward(request, response);
        } catch (Exception e) {
            e.printStackTrace();
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Error loading user history.");
        }
    }
} 