<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Register - VoidQuiz</title>
    <style>
        body {
            background-color: #0e0b39;
            color: #f0e6ff;
            font-family: 'Segoe UI', sans-serif;
            display: flex;
            flex-direction: column;
            align-items: center;
            justify-content: center;
            height: 100vh;
            margin: 0;
        }

        .brand {
            font-size: 3rem;
            margin-bottom: 30px;
            font-weight: bold;
            color: #bdaaff;
        }

        .register-box {
            background-color: #161244;
            padding: 40px;
            border-radius: 10px;
            width: 350px;
            box-shadow: 0 0 15px rgba(0, 0, 0, 0.3);
            display: flex;
            flex-direction: column;
            align-items: center;
        }

        .title {
            font-size: 1.6rem;
            margin-bottom: 20px;
            color: #f0e6ff;
        }

        form {
            width: 100%;
        }

        input[type="text"],
        input[type="password"],
        input[type="submit"] {
            width: 100%;
            padding: 12px;
            margin: 10px 0;
            border: none;
            border-radius: 6px;
            font-size: 1rem;
            box-sizing: border-box;
        }

        input[type="text"],
        input[type="password"] {
            background-color: #221e5e;
            color: #fff;
        }

        input[type="submit"] {
            background-color: #3f36c4;
            color: white;
            font-weight: bold;
            cursor: pointer;
        }

        input[type="submit"]:hover {
            background-color: #5c52f0;
        }

        .error {
            color: #ff8080;
            margin-bottom: 10px;
            text-align: center;
        }

        .link {
            text-align: center;
            margin-top: 15px;
        }

        .link a {
            color: #bdaaff;
            text-decoration: underline;
        }
    </style>
</head>
<body>

<div class="brand">VoidQuiz</div>

<div class="register-box">
    <div class="title">Register</div>

    <% if (request.getAttribute("error") != null) { %>
    <div class="error"><%= request.getAttribute("error") %></div>
    <% } %>

    <form action="register" method="post">
        <input type="text" name="username" placeholder="Username" required />
        <input type="password" name="password" placeholder="Password" required />
        <input type="submit" value="Register" />
    </form>

    <div class="link">
        Already have an account? <a href="login.jsp">Login here</a>
    </div>
</div>

</body>
</html>