<%
    HttpSession ses = request.getSession(false);
    if (ses != null && ses.getAttribute("username") != null) {
        response.sendRedirect("welcome.jsp");
    }
%>
<html>
<head>
    <title>Register</title>
    <style>
        body {
            background-color: #e6f0ff;
            font-family: 'Segoe UI', sans-serif;
            display: flex;
            justify-content: center;
            align-items: center;
            height: 100vh;
        }

        .container {
            background-color: white;
            padding: 40px;
            border-radius: 12px;
            box-shadow: 0 4px 20px rgba(0, 0, 0, 0.1);
            width: 350px;
        }

        h2 {
            text-align: center;
            color: #005bb5;
        }

        label {
            display: block;
            margin-top: 20px;
            font-weight: 600;
        }

        input[type="text"],
        input[type="password"] {
            width: 100%;
            padding: 10px;
            margin-top: 8px;
            border: 1px solid #ccc;
            border-radius: 8px;
        }

        input[type="submit"] {
            margin-top: 30px;
            width: 100%;
            padding: 10px;
            background-color: #005bb5;
            color: white;
            border: none;
            border-radius: 8px;
            cursor: pointer;
            font-weight: bold;
        }

        input[type="submit"]:hover {
            background-color: #005bb5;
        }

        .link {
            text-align: center;
            margin-top: 20px;
        }

        .link a {
            color: #0073e6;
            text-decoration: none;
        }

        .link a:hover {
            text-decoration: underline;
        }

        .error {
            color: #cc0000;
            font-size: 0.9em;
            text-align: center;
            margin-top: 10px;
        }
    </style>
</head>
<body>
<div class="container">
    <h2>Register</h2>
    <form action="register" method="post">
        <label>Username:</label>
        <input type="text" name="username" required>

        <label>Password:</label>
        <input type="password" name="password" required>

        <input type="submit" value="Register">
    </form>

    <%
        String error = (String) request.getAttribute("error");
        if (error != null) {
    %>
    <div class="error"><%= error %>
    </div>
    <%
        }
    %>

    <div class="link">
        Already have an account? <a href="login.jsp">Log in</a>
    </div>
</div>
</body>
</html>