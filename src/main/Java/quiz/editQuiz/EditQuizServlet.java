package quiz.editQuiz;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/EditQuizServlet")
public class EditQuizServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        response.getWriter().println("<html><head><title>Edit Quiz</title></head><body " +
                "style='font-family:Segoe UI,Tahoma,Geneva,Verdana,sans-serif;background:" +
                "linear-gradient(120deg,#e0e7ff 0%,#f3e8ff 100%);min-height:100vh;'><div style=" +
                "'max-width:440px;margin:80px auto;background:#fff;border-radius:16px;border" +
                ":1.5px solid #e3e8ee;box-shadow:0 6px 32px rgba(0,0,0,0.10);padding:40px 32px 28px " +
                "32px;text-align:center;'><h2 style='color:#2d3a4b;'>Edit Quiz</h2><p style='color" +
                ":#4a5a6a;font-size:16px;'>Sorry, editing quizzes is not available yet.</p></div>" +
                "</body></html>");
    }
} 