package quiz.quizSummary;

import org.apache.commons.dbcp2.BasicDataSource;
import quiz.Quiz;
import quiz.QuizRepository;
import quizAttempt.QuizAttempt;
import quizAttempt.QuizAttemptRepository;
import repository.UserRepository;
import resources.DatabaseConnection;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.*;

@WebServlet("/QuizSummaryServlet")
public class QuizSummaryServlet extends HttpServlet {
    private QuizRepository quizRepository;
    private UserRepository userRepository;
    private QuizAttemptRepository quizAttemptRepository;

    @Override
    public void init() {
        BasicDataSource dataSource = DatabaseConnection.getDataSource();
        quizRepository = new QuizRepository(dataSource);
        userRepository = new UserRepository(dataSource);
        quizAttemptRepository = new QuizAttemptRepository(dataSource);
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String quizIdStr = request.getParameter("quizId");

        if (quizIdStr == null) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Quiz ID is required.");
            return;
        }

        try {
            long quizId = Long.parseLong(quizIdStr);
            Quiz quiz = quizRepository.getById(quizId);
            if (quiz == null) {
                response.sendError(HttpServletResponse.SC_NOT_FOUND, "Quiz not found.");
                return;
            }

            getQuizSummary(request, quiz);
            request.getRequestDispatcher("/quizSummary.jsp").forward(request, response);

        } catch (NumberFormatException e) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid Quiz ID.");
        } catch (Exception e) {
            e.printStackTrace();
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Error loading quiz summary.");
        }
    }

    private void getQuizSummary(HttpServletRequest request, Quiz quiz) {
        Long creatorId = (long) quiz.getCreatorId();
        String creatorName = getCreatorName(creatorId);

        HttpSession session = request.getSession(false);
        Long userId = getUserIdFromSession(session);
        String sort = getSortParameter(request);

        List<QuizAttempt> userAttempts = getUserAttempts(userId, quiz.getQuizId(), sort);
        List<QuizAttempt> topAllTime = getTopAllTimePerformers(quiz.getQuizId());
        List<QuizAttempt> topRecent = getTopRecentPerformers(quiz.getQuizId());
        List<QuizAttempt> recentAttempts = getRecentAttempts(quiz.getQuizId());

        Map<Long, String> userIdToUsername = buildUsernameMap(topAllTime, topRecent, recentAttempts, creatorId);
        Map<String, Object> stats = quizAttemptRepository.getQuizStats(quiz.getQuizId());
        //System.out.println(stats);
        boolean isOwner = checkIsOwner(userId, creatorId);

        setRequestAttributes(request, quiz, creatorName, userAttempts, topAllTime, topRecent,
                recentAttempts, stats, isOwner, sort, userIdToUsername);
    }

    private String getCreatorName(Long creatorId) {
        return userRepository.getUsernameById(creatorId);
    }

    private String getSortParameter(HttpServletRequest request) {
        String sort = request.getParameter("sort");
        if (sort == null) sort = "date";
        return sort;
    }

    private List<QuizAttempt> getTopAllTimePerformers(long quizId) {
        return quizAttemptRepository.getTopPerformers(quizId, 10, null);
    }

    private List<QuizAttempt> getTopRecentPerformers(long quizId) {
        LocalDateTime since = LocalDateTime.now().minusMinutes(15);
        Timestamp sinceTimestamp = Timestamp.valueOf(since);
        return quizAttemptRepository.getTopPerformers(quizId, 10, sinceTimestamp);
    }

    private List<QuizAttempt> getRecentAttempts(long quizId) {
        return quizAttemptRepository.getRecentAttempts(quizId, 10);
    }

    private Map<Long, String> buildUsernameMap(List<QuizAttempt> topAllTime, List<QuizAttempt> topRecent,
                                               List<QuizAttempt> recentAttempts, Long creatorId) {
        Set<Long> userIds = new HashSet<>();
        for (QuizAttempt a : topAllTime) {
            userIds.add(Long.valueOf(a.getUserId()));
        }
        for (QuizAttempt a : topRecent){
            userIds.add(Long.valueOf(a.getUserId()));
        }
        for (QuizAttempt a : recentAttempts){
            userIds.add(Long.valueOf(a.getUserId()));
        }
        userIds.add(creatorId);

        Map<Long, String> userIdToUsername = new HashMap<>();
        for (Long uid : userIds) {
            String uname = userRepository.getUsernameById(uid);
            if (uname != null) userIdToUsername.put(uid, uname);
        }
        return userIdToUsername;
    }

    private boolean checkIsOwner(Long userId, Long creatorId) {
        return (userId != null && userId.equals(creatorId));
    }

    private void setRequestAttributes(HttpServletRequest request, Quiz quiz, String creatorName,
                                      List<QuizAttempt> userAttempts, List<QuizAttempt> topAllTime,
                                      List<QuizAttempt> topRecent, List<QuizAttempt> recentAttempts,
                                      Map<String, Object> stats, boolean isOwner, String sort,
                                      Map<Long, String> userIdToUsername) {
        request.setAttribute("quiz", quiz);
        request.setAttribute("creatorName", creatorName);
        request.setAttribute("userAttempts", userAttempts);
        request.setAttribute("topAllTime", topAllTime);
        request.setAttribute("topRecent", topRecent);
        request.setAttribute("recentAttempts", recentAttempts);
        request.setAttribute("stats", stats);
        request.setAttribute("isOwner", isOwner);
        request.setAttribute("sort", sort);
        request.setAttribute("userIdToUsername", userIdToUsername);
    }

    private Long getUserIdFromSession(HttpSession session) {
        if (session == null) return null;
        Object id = session.getAttribute("userId");
        return (id instanceof Long) ? (Long) id : null;
    }

    private List<QuizAttempt> getUserAttempts(Long userId, long quizId, String sort) {
        if (userId == null) return Collections.emptyList();
        return quizAttemptRepository.getAttemptsByUserAndQuiz(userId, quizId, sort);
    }
}