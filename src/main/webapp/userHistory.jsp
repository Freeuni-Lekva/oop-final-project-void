<%@ page import="java.util.List" %>
<%@ page import="java.util.Map" %>
<%@ page import="quiz.Quiz" %>
<%@ page import="quizAttempt.QuizAttempt" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>My Quiz History</title>
    <style>
        body {
            background: #0e0b39;
            min-height: 100vh;
            font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
            margin: 0;
            padding: 0;
        }
        .page {
            max-width: 1100px;
            margin: 40px auto 40px auto;
            padding: 0 16px;
        }
        .section-card {
            background: #fff;
            border-radius: 16px;
            border: 1.5px solid #e3e8ee;
            box-shadow: 0 6px 32px rgba(0,0,0,0.10);
            padding: 32px 32px 24px 32px;
            margin-bottom: 32px;
        }
        h2 {
            text-align: left;
            color: #2d3a4b;
            margin-bottom: 12px;
        }
        .section-title {
            color: #2d3a4b;
            font-size: 17px;
            font-weight: 600;
            margin-bottom: 10px;
        }
        .info-card {
            background: #f9fafb;
            border: 1.5px solid #e3e8ee;
            border-radius: 8px;
            padding: 16px 18px;
            margin-bottom: 16px;
            font-size: 15px;
            color: #2d3a4b;
        }
        table {
            width: 100%;
            border-collapse: collapse;
            margin-bottom: 12px;
        }
        th, td {
            padding: 8px 10px;
            text-align: left;
            border-bottom: 1px solid #e3e8ee;
        }
        th {
            background: #f3f6fa;
            color: #2d3a4b;
            font-weight: 600;
        }
        tr:hover {
            background: #f5f7fb;
        }
        .quiz-link {
            color: #1976d2;
            text-decoration: none;
            font-weight: 500;
        }
        .quiz-link:hover {
            text-decoration: underline;
        }
        .no-history {
            color: #6b7a89;
            text-align: center;
            margin-top: 24px;
            font-size: 16px;
        }
        input[type="text"], textarea {
            width: 100%;
            padding: 8px 10px;
            margin-top: 4px;
            margin-bottom: 18px;
            border: 1px solid #cfd8dc;
            border-radius: 6px;
            font-size: 15px;
            background: #e3f2fd;
            transition: border 0.2s;
        }
    </style>
</head>
<body>
<div class="page">
    <div class="section-card">
        <h2>My Quiz History</h2>
        <div class="info-card">
            <%
                List<QuizAttempt> userAttempts = (List<QuizAttempt>) request.getAttribute("userAttempts");
                Map<Long, Quiz> quizDetails = (Map<Long, Quiz>) request.getAttribute("quizDetails");

                if (userAttempts == null || userAttempts.isEmpty()) {
            %>
                <div class="no-history">You haven't taken any quizzes yet.</div>
            <%
                } else {
            %>
            <table>
                <tr>
                    <th>Quiz Name</th>
                    <th>Score</th>
                    <th>Percent Correct</th>
                    <th>Time Taken (s)</th>
                    <th>Date</th>
                </tr>
                <%
                    for (QuizAttempt attempt : userAttempts) {
                        Long quizId = Long.valueOf(attempt.getQuizId());
                        Quiz quiz = quizDetails.get(quizId);
                        String quizName = (quiz != null) ? quiz.getTitle() : "Unknown Quiz";
                %>
                <tr>
                    <td>
                        <a class="quiz-link" href="QuizSummaryServlet?quizId=<%= quizId %>">
                            <%= quizName %>
                        </a>
                    </td>
                    <td><%= attempt.getScore() %></td>
                    <td><%= attempt.getTotalQuestions() > 0 ? String.format("%.1f", 100.0 * attempt.getScore() / attempt.getTotalQuestions()) : "-" %>%</td>
                    <td><%= attempt.getTimeTaken() %></td>
                    <td><%= attempt.getAttemptedAt() != null ? attempt.getAttemptedAt().toString().substring(0, 16).replace('T', ' ') : "" %></td>
                </tr>
                <%
                    }
                %>
            </table>
            <%
                }
            %>
        </div>
    </div>
</div>
</body>
</html> 