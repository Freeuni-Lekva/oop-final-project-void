package quiz.submitQuiz.servlets;

import choice.ChoiceRepository;
import choice.ChoiceService;
import com.google.gson.Gson;
import questionAnswer.QuestionAnswerRepository;
import questionAnswer.QuestionAnswerService;
import quiz.submitQuiz.QuizEvaluationService;
import quiz.submitQuiz.dtos.QuizSubmissionDto;
import quiz.submitQuiz.mapper.QuizSubmissionMapper;
import quizAttempt.QuizAttempt;
import quizAttempt.QuizAttemptRepository;
import quizAttempt.QuizAttemptService;
import resources.DatabaseConnection;
import response.Response;
import response.ResponseRepository;
import response.ResponseService;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

public class SubmitQuizServlet extends HttpServlet {
    QuizSubmissionDto submission;

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        BufferedReader reader = req.getReader();
        HttpSession session = req.getSession();
        submission = new Gson().fromJson(reader, QuizSubmissionDto.class);
        int score;
        Integer userId = (Integer) req.getSession().getAttribute("userId");

        QuestionAnswerRepository qaRepository = new QuestionAnswerRepository(DatabaseConnection.getDataSource());
        QuizAttemptRepository attemptRepository = new QuizAttemptRepository(DatabaseConnection.getDataSource());
        ResponseRepository responseRepository = new ResponseRepository(DatabaseConnection.getDataSource());
        ChoiceRepository choiceRepository = new ChoiceRepository(DatabaseConnection.getDataSource());

        ResponseService responseService = new ResponseService(responseRepository);

        QuizEvaluationService evaluationService = new QuizEvaluationService(new QuestionAnswerService(qaRepository), new ChoiceService(choiceRepository));
        QuizAttemptService attemptService = new QuizAttemptService(attemptRepository);

        score= evaluationService.calculateScore(submission.answers);
        int totalQuestions = submission.totalQuestions;
        session.setAttribute("score", score);
        session.setAttribute("totalQuestions", totalQuestions);

        QuizAttempt attempt = QuizSubmissionMapper.mapToQuizAttempt(submission,userId);
        attempt.setScore(score);

        attemptService.createQuizAttempt(attempt);

        List<Response> responses = QuizSubmissionMapper.mapToResponses(submission, attempt.getAttemptId());
        for (Response response : responses) {
            responseService.createResponse(response);
        }

    }


    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession();
        int score = (int) session.getAttribute("score");
        int totalQuestions = (int) session.getAttribute("totalQuestions");

        session.removeAttribute("score");
        session.removeAttribute("totalQuestions");
        resp.setContentType("text/html;charset=UTF-8");
        PrintWriter out = resp.getWriter();

        out.println("<!DOCTYPE html>");
        out.println("<html lang='en'>");
        out.println("<head>");
        out.println("<meta charset='UTF-8'>");
        out.println("<title>Quiz Result</title>");
        out.println("<link rel='stylesheet' href='/css/quiz/takeQuiz/quizResult.css'>");
        out.println("</head>");
        out.println("<body>");

        out.println("<script>");
        out.println("const score = " + score + ";");
        out.println("const totalQuestions = " + totalQuestions + ";");
        out.println("</script>");

        out.println("<div class='container'>");
        out.println("<h1>Quiz Result</h1>");
        out.println("<div id='result'></div>");
        out.println("<button id='home-btn'>Go to Homepage</button>");
        out.println("</div>");

        out.println("<script src='/script/quiz/takeQuiz/quizResult.js'></script>");
        out.println("</body>");
        out.println("</html>");
    }

}
