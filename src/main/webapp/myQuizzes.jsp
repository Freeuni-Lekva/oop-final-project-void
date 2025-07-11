<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
    if (session == null || session.getAttribute("username") == null) {
        response.sendRedirect("login.jsp");
        return;
    }

    String username = (String) session.getAttribute("username");
%>
<%@ page import="java.util.List" %>
<%@ page import="java.util.Map" %>
<%@ page import="quiz.Quiz" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>My Quizzes</title>
    <style>
        body {
            background: #0e0b39;
            min-height: 100vh;
            font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
            margin: 0;
            padding: 0;
        }
        .container {
            max-width: 900px;
            margin: 50px auto;
            background: #fff;
            border-radius: 16px;
            border: 1.5px solid #e3e8ee;
            box-shadow: 0 6px 32px rgba(0,0,0,0.10);
            padding: 40px 32px 28px 32px;
        }
        h2 {
            text-align: center;
            color: #2d3a4b;
            margin-bottom: 24px;
        }
        .quiz-list {
            display: flex;
            flex-wrap: wrap;
            gap: 28px;
            justify-content: center;
        }
        .quiz-card {
            background: #f9fafb;
            border: 1.5px solid #e3e8ee;
            border-radius: 12px;
            box-shadow: 0 2px 12px rgba(25, 118, 210, 0.06);
            width: 290px;
            padding: 24px 20px 18px 20px;
            display: flex;
            flex-direction: column;
            align-items: flex-start;
            transition: box-shadow 0.2s, transform 0.1s;
        }
        .quiz-card:hover {
            box-shadow: 0 6px 24px rgba(25, 118, 210, 0.13);
            transform: translateY(-2px) scale(1.02);
        }
        .quiz-title {
            font-size: 20px;
            font-weight: 700;
            color: #1976d2;
            margin-bottom: 8px;
            text-decoration: none;
        }
        .quiz-desc {
            color: #4a5a6a;
            font-size: 15px;
            margin-bottom: 12px;
        }
        .quiz-meta {
            font-size: 13px;
            color: #6b7a89;
            margin-bottom: 10px;
        }
        .creator-link {
            color: #1976d2;
            text-decoration: none;
            font-weight: 500;
        }
        .creator-link:hover {
            text-decoration: underline;
        }
        .take-quiz-btn {
            margin-top: 10px;
            background: linear-gradient(90deg, #1976d2 0%, #42a5f5 100%);
            color: #fff;
            border: none;
            border-radius: 8px;
            padding: 10px 22px;
            font-size: 15px;
            font-weight: 600;
            cursor: pointer;
            box-shadow: 0 2px 8px rgba(25, 118, 210, 0.10);
            transition: background 0.2s, box-shadow 0.2s, transform 0.1s;
        }
        .take-quiz-btn:hover {
            background: linear-gradient(90deg, #1565c0 0%, #1976d2 100%);
            box-shadow: 0 4px 16px rgba(25, 118, 210, 0.18);
            transform: translateY(-2px) scale(1.03);
        }
        .no-quizzes {
            color: #4a5a6a;
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
<div class="container">
    <h2>My Quizzes</h2>
    <%
        List<Quiz> quizzes = (List<Quiz>) request.getAttribute("quizzes");
        Map<Long, String> userIdToUsername = (Map<Long, String>) request.getAttribute("userIdToUsername");
        if (quizzes == null || quizzes.isEmpty()) {
    %>
        <div class="no-quizzes">You haven't created any quizzes yet.</div>
    <%
        } else {
    %>
        <div class="quiz-list">
        <%
            for (Quiz quiz : quizzes) {
                Long creatorId = Long.valueOf(quiz.getCreatorId());
                String creatorName = userIdToUsername != null ? userIdToUsername.get(creatorId) : (creatorId != null ? ("User " + creatorId) : "Unknown");
        %>
            <div class="quiz-card">
                <a class="quiz-title" href="QuizSummaryServlet?quizId=<%= quiz.getQuizId() %>"><%= quiz.getTitle() %></a>
                <div class="quiz-desc"><%= quiz.getDescription() %></div>
                <div class="quiz-meta">
                    By <a class="creator-link" href="userProfile?userId=<%= creatorId %>"><%= creatorName %></a><br>
                    Created: <%= quiz.getCreatedAt() != null ? quiz.getCreatedAt().toString().substring(0, 16).replace('T', ' ') : "Unknown" %>
                </div>
                <form action="quiz/start" method="get" style="margin:0;">
                    <input type="hidden" name="id" value="<%= quiz.getQuizId() %>" />
                    <button class="take-quiz-btn" type="submit">Take Quiz</button>
                </form>
            </div>
        <%
            }
        %>
        </div>
    <%
        }
    %>
</div>
</body>
</html> 