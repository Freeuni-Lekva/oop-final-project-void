<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
    if (session == null || session.getAttribute("username") == null) {
        response.sendRedirect("login.jsp");
        return;
    }

    String username = (String) session.getAttribute("username");
%>

<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Welcome - VoidQuiz</title>
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
            padding: 40px;
            border-radius: 12px;
            margin-top: 30px;
            width: 400px;
            box-shadow: 0 0 15px rgba(0, 0, 0, 0.25);
            text-align: center;
        }

        .card h2 {
            margin-bottom: 20px;
            font-size: 1.8rem;
            color: #f0e6ff;
        }

        .card p {
            font-size: 1.1rem;
            margin-bottom: 30px;
        }

        .logout-btn {
            background-color: #3f36c4;
            color: white;
            border: none;
            padding: 12px 20px;
            border-radius: 6px;
            font-weight: bold;
            cursor: pointer;
            transition: background-color 0.3s;
        }

        .logout-btn:hover {
            background-color: #5c52f0;
        }

        .confirm-box {
            display: none;
            margin-top: 20px;
        }

        .confirm-box button {
            margin: 5px;
            padding: 10px 16px;
            border: none;
            border-radius: 6px;
            font-weight: bold;
            cursor: pointer;
        }

        .yes {
            background-color: #c84646;
            color: white;
        }

        .no {
            background-color: #5555aa;
            color: white;
        }

    </style>
    <script>
        function showConfirm() {
            document.getElementById("confirmBox").style.display = "block";
        }

        function cancelLogout() {
            document.getElementById("confirmBox").style.display = "none";
        }
    </script>
</head>
<body>

<div class="navbar">
    <a href="<%= request.getContextPath() %>/allQuizzes">All Quizzes</a>
    <a href="<%= request.getContextPath() %>/createQuiz.jsp">Create Quiz</a>
    <a href="<%= request.getContextPath() %>/myQuizzes">My Quizzes</a>
    <a href="<%= request.getContextPath() %>/search_bar.html">Search Users</a>
    <a href="<%= request.getContextPath() %>/inbox.html">Inbox</a>
    <a href="<%= request.getContextPath() %>/friends.html">Friends</a>
    <a href="<%= request.getContextPath() %>/messageInbox.html">Message Inbox</a>
</div>

<div class="header">VoidQuiz</div>

<div class="card">
    <h2>Welcome, <%= username %>!</h2>
    <p>You are now logged in. Create quizzes, browse your friends’ quizzes, and more.</p>
    <button class="logout-btn" onclick="showConfirm()">Log Out</button>

    <div id="confirmBox" class="confirm-box">
        <p>Are you sure you want to log out?</p>
        <form action="logout" method="get" style="display: inline;">
            <button type="submit" class="yes">Yes</button>
        </form>
        <button class="no" onclick="cancelLogout()">No</button>
    </div>
</div>

</body>
</html>
