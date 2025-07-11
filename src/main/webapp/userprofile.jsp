<%@ page contentType="text/html;charset=UTF-8" language="java" isELIgnored="false" %>
<%@ page import="javax.servlet.http.HttpSession" %>
<%@ page import="java.sql.*" %>
<%@ page import="resources.DatabaseConnection" %>

<%
  HttpSession ses = request.getSession(false);
  String username = (String) request.getAttribute("searchedUser");
  if (username == null) {
    username = (String) session.getAttribute("username");
  }

  if (username == null) {
    response.sendRedirect("login.jsp");
    return;
  }

  boolean isAdmin = false;
  String joinDate = "Unknown";
  Integer quizTakenCount = (Integer) request.getAttribute("quizTakenCount");
  String name = (String) request.getAttribute("searchedUser");

  try {
    Connection conn = DatabaseConnection.getDataSource().getConnection();
    PreparedStatement stmt = conn.prepareStatement("SELECT user_id, is_admin, created_at FROM users WHERE username = ?");
    stmt.setString(1, name);
    ResultSet rs = stmt.executeQuery();
    long userId = -1;
    if (rs.next()) {
      isAdmin = rs.getBoolean("is_admin");
      joinDate = rs.getTimestamp("created_at").toString();
      userId = rs.getLong("user_id");
    }
    rs.close();
    stmt.close();
    conn.close();
  } catch (Exception e) {
    e.printStackTrace();
  }
%>

<!DOCTYPE html>
<html>
<head>
  <meta charset="UTF-8">
  <title>Profile - VoidQuiz</title>
  <style>
    body {
      background-color: #0e0b39;
      color: #f0e6ff;
      font-family: 'Segoe UI', sans-serif;
      margin: 0;
      padding: 0;
      display: flex;
      flex-direction: column;
      align-items: center;
      height: 100vh;
    }

    .navbar {
      background-color: #1d1a4d;
      width: 100%;
      padding: 12px 0;
      display: flex;
      justify-content: center;
      gap: 30px;
      position: fixed;
      top: 0;
      left: 0;
      z-index: 1000;
    }

    .navbar a {
      color: #f0e6ff;
      text-decoration: none;
      font-weight: bold;
      padding: 10px 16px;
      border-radius: 8px;
      transition: background-color 0.3s;
    }

    .navbar a:hover {
      background-color: #302a84;
    }

    .header {
      margin-top: 100px;
      font-size: 3rem;
      font-weight: bold;
      color: #bdaaff;
    }

    .card {
      background-color: #161244;
      padding: 30px;
      border-radius: 12px;
      margin-top: 30px;
      width: 380px;
      box-shadow: 0 0 15px rgba(0, 0, 0, 0.25);
      text-align: left;
    }

    .card h2 {
      margin-bottom: 20px;
      font-size: 1.8rem;
      color: #f0e6ff;
    }

    .profile-item {
      margin-bottom: 15px;
      font-size: 1.1rem;
    }

    .profile-item span {
      font-weight: bold;
      color: #bdaaff;
    }
  </style>
</head>
<body>

<div class="navbar">
  <a href="<%= request.getContextPath() %>/allQuizzes">All Quizzes</a>
  <a href="<%= request.getContextPath() %>/createQuiz.jsp">Create Quiz</a>
  <a href="<%= request.getContextPath() %>/myQuizzes">My Quizzes</a>
  <a href="<%= request.getContextPath() %>/search_bar.html">Search Users</a>
  <a href="<%= request.getContextPath() %>/inbox.html">Inbox</a>
  <a href="<%= request.getContextPath() %>/friends.html">Friends</a>
</div>

<div class="header">VoidQuiz</div>

<div class="card">
  <h2>Profile</h2>
  <div class="profile-item"><span>Username:</span> <%= name %></div>
  <div class="profile-item"><span>Admin:</span> <%= isAdmin ? "Yes" : "No" %></div>
  <div class="profile-item"><span>Quizzes Taken:</span> <%= quizTakenCount %></div>
  <div class="profile-item"><span>Join Date:</span> <%= joinDate %></div>
</div>

</body>
</html>