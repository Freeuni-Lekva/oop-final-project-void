<%@ page import="java.util.List" %>
<%@ page import="java.util.Map" %>
<%@ page import="quiz.Quiz" %>
<%@ page import="quizAttempt.QuizAttempt" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Quiz Summary</title>
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
        .quiz-meta {
            color: #6b7a89;
            font-size: 15px;
            margin-bottom: 18px;
        }
        .creator-link {
            color: #1976d2;
            text-decoration: none;
            font-weight: 500;
        }
        .creator-link:hover {
            text-decoration: underline;
        }
        .desc {
            color: #4a5a6a;
            font-size: 16px;
            margin-bottom: 18px;
        }
        .actions {
            display: flex;
            gap: 18px;
            margin-bottom: 18px;
        }
        .action-btn {
            background: linear-gradient(90deg, #1976d2 0%, #42a5f5 100%);
            color: #fff;
            border: none;
            border-radius: 8px;
            padding: 12px 0;
            width: 160px;
            font-size: 16px;
            font-weight: 700;
            cursor: pointer;
            box-shadow: 0 2px 8px rgba(25, 118, 210, 0.10);
            transition: background 0.2s, box-shadow 0.2s, transform 0.1s;
        }
        .action-btn:hover {
            background: linear-gradient(90deg, #1565c0 0%, #1976d2 100%);
            box-shadow: 0 4px 16px rgba(25, 118, 210, 0.18);
            transform: translateY(-2px) scale(1.03);
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
        .stat-box {
            display: inline-block;
            background: #f9fafb;
            border: 1.5px solid #e3e8ee;
            border-radius: 8px;
            padding: 12px 18px;
            margin: 0 10px 10px 0;
            font-size: 15px;
            color: #2d3a4b;
        }
        .sort-links {
            margin-bottom: 10px;
            text-align: right;
        }
        .sort-link {
            color: #1976d2;
            text-decoration: none;
            margin-left: 10px;
            font-size: 14px;
        }
        .sort-link.selected {
            font-weight: bold;
            text-decoration: underline;
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
    <%
        Quiz quiz = (Quiz) request.getAttribute("quiz");
        String creatorName = (String) request.getAttribute("creatorName");
        boolean isOwner = Boolean.TRUE.equals(request.getAttribute("isOwner"));
        String sort = (String) request.getAttribute("sort");
        Map<Long, String> userIdToUsername = (Map<Long, String>) request.getAttribute("userIdToUsername");
    %>

    <div class="section-card">
        <h2><%= quiz.getTitle() %></h2>
        <div class="quiz-meta">
            By <a class="creator-link" href="UserProfileServlet?userId=<%= quiz.getCreatorId() %>"><%= creatorName %></a>
            | Created: <%= quiz.getCreatedAt() != null ? quiz.getCreatedAt().toString().substring(0, 16).replace('T', ' ') : "Unknown" %>
        </div>
        <div class="info-card desc"><%= quiz.getDescription() %></div>
        <div class="actions">
            <form action="quiz/start" method="get" style="margin:0;">
                <input type="hidden" name="id" value="<%= quiz.getQuizId() %>" />
                <button class="action-btn" type="submit">Take Quiz</button>
            </form>
            <% if (isOwner) { %>
            <form action="EditQuizServlet" method="get" style="margin:0;">
                <input type="hidden" name="quizId" value="<%= quiz.getQuizId() %>" />
                <button class="action-btn" type="submit">Edit Quiz</button>
            </form>
            <% } %>
        </div>
    </div>


    <div class="section-card">
        <div class="section-title">Your Past Attempts</div>
        <div class="info-card">
            <div class="sort-links">
                Sort by:
                <a class="sort-link <%= "date".equals(sort) ? "selected" : "" %>" href="QuizSummaryServlet?quizId=<%= quiz.getQuizId() %>&sort=date">Date</a>
                <a class="sort-link <%= "percent".equals(sort) ? "selected" : "" %>" href="QuizSummaryServlet?quizId=<%= quiz.getQuizId() %>&sort=percent">Percent Correct</a>
                <a class="sort-link <%= "time".equals(sort) ? "selected" : "" %>" href="QuizSummaryServlet?quizId=<%= quiz.getQuizId() %>&sort=time">Time Taken</a>
            </div>
            <%
                List<QuizAttempt> userAttempts = (List<QuizAttempt>) request.getAttribute("userAttempts");
                if (userAttempts == null || userAttempts.isEmpty()) {
            %>
                <div style="color:#6b7a89;">No attempts yet.</div>
            <%
                } else {
            %>
            <table>
                <tr>
                    <th>Date</th>
                    <th>Score</th>
                    <th>Percent Correct</th>
                    <th>Time Taken (s)</th>
                </tr>
                <%
                    for (QuizAttempt attempt : userAttempts) {
                %>
                <tr>
                    <td><%= attempt.getAttemptedAt() != null ? attempt.getAttemptedAt().toString().substring(0, 16).replace('T', ' ') : "" %></td>
                    <td><%= attempt.getScore() %></td>
                    <td><%= attempt.getTotalQuestions() > 0 ? String.format("%.1f", 100.0 * attempt.getScore() / attempt.getTotalQuestions()) : "-" %>%</td>
                    <td><%= attempt.getTimeTaken() %></td>
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


    <div class="section-card">
        <div class="section-title">All-Time Top Performers</div>
        <div class="info-card">
        <%
            List<QuizAttempt> topAllTime = (List<QuizAttempt>) request.getAttribute("topAllTime");
            if (topAllTime == null || topAllTime.isEmpty()) {
        %>
            <div style="color:#6b7a89;">No attempts yet.</div>
        <%
            } else {
        %>
        <table>
            <tr>
                <th>User</th>
                <th>Score</th>
                <th>Percent Correct</th>
                <th>Time Taken (s)</th>
                <th>Date</th>
            </tr>
            <%
                for (QuizAttempt attempt : topAllTime) {
                    Long uid = Long.valueOf(attempt.getUserId());
                    String uname = (userIdToUsername != null && userIdToUsername.get(uid) != null) ? userIdToUsername.get(uid) : "Unknown";
            %>
            <tr>
                <td><a class="creator-link" href="UserProfileServlet?userId=<%= uid %>"><%= uname %></a></td>
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

    <div class="section-card">
        <div class="section-title">Top Performers (Last 15 Minutes)</div>
        <div class="info-card">
        <%
            List<QuizAttempt> topRecent = (List<QuizAttempt>) request.getAttribute("topRecent");
            if (topRecent == null || topRecent.isEmpty()) {
        %>
            <div style="color:#6b7a89;">No attempts in the last 15 minutes.</div>
        <%
            } else {
        %>
        <table>
            <tr>
                <th>User</th>
                <th>Score</th>
                <th>Percent Correct</th>
                <th>Time Taken (s)</th>
                <th>Date</th>
            </tr>
            <%
                for (QuizAttempt attempt : topRecent) {
                    Long uid = Long.valueOf(attempt.getUserId());
                    String uname = (userIdToUsername != null && userIdToUsername.get(uid) != null) ? userIdToUsername.get(uid) : "Unknown";
            %>
            <tr>
                <td><a class="creator-link" href="UserProfileServlet?userId=<%= uid %>"><%= uname %></a></td>
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

    <div class="section-card">
        <div class="section-title">Recent Test Takers</div>
        <div class="info-card">
        <%
            List<QuizAttempt> recentAttempts = (List<QuizAttempt>) request.getAttribute("recentAttempts");
            if (recentAttempts == null || recentAttempts.isEmpty()) {
        %>
            <div style="color:#6b7a89;">No recent attempts.</div>
        <%
            } else {
        %>
        <table>
            <tr>
                <th>User</th>
                <th>Score</th>
                <th>Percent Correct</th>
                <th>Time Taken (s)</th>
                <th>Date</th>
            </tr>
            <%
                for (QuizAttempt attempt : recentAttempts) {
                    Long uid = Long.valueOf(attempt.getUserId());
                    String uname = (userIdToUsername != null && userIdToUsername.get(uid) != null) ? userIdToUsername.get(uid) : "Unknown";
            %>
            <tr>
                <td><a class="creator-link" href="UserProfileServlet?userId=<%= uid %>"><%= uname %></a></td>
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


    <div class="section-card">
        <div class="section-title">Summary Statistics</div>
        <div class="info-card">
            <%
                Map<String, Object> stats = (Map<String, Object>) request.getAttribute("stats");
                if (stats == null || stats.isEmpty()) {
            %>
                <span style="color:#6b7a89;">No statistics available.</span>
            <%
                } else {
            %>
                <span class="stat-box">Attempts: <%= stats.get("total_attempts") %></span>
                <span class="stat-box">Average Score: <%= stats.get("average_score") %></span>
                <span class="stat-box">Highest Score: <%= stats.get("highest_score") %></span>
            <%
                }
            %>
        </div>
    </div>
</div>
</body>
</html> 