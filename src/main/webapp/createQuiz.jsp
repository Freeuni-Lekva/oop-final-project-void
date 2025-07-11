<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Create New Quiz</title>
    <style>
        body {
            background: linear-gradient(120deg, #e0e7ff 0%, #f3e8ff 100%);
            min-height: 100vh;
            font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
            margin: 0;
            padding: 0;
        }
        .container {
            max-width: 440px;
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
            margin-bottom: 12px;
        }
        p, ul {
            color: #4a5a6a;
            font-size: 15px;
        }
        form {
            margin-top: 18px;
        }
        label {
            font-weight: 500;
            color: #2d3a4b;
        }
        input[type="text"], textarea {
            width: 100%;
            padding: 8px 10px;
            margin-top: 4px;
            margin-bottom: 18px;
            border: 1px solid #cfd8dc;
            border-radius: 6px;
            font-size: 15px;
            background: #f9fafb;
            transition: border 0.2s;
        }
        input[type="text"]:focus, textarea:focus {
            border: 1.5px solid #1976d2;
            outline: none;
        }
        input[type="submit"] {
            background: linear-gradient(90deg, #1976d2 0%, #42a5f5 100%);
            color: #fff;
            border: none;
            border-radius: 8px;
            padding: 12px 0;
            width: 100%;
            font-size: 17px;
            font-weight: 700;
            cursor: pointer;
            box-shadow: 0 2px 8px rgba(25, 118, 210, 0.10);
            transition: background 0.2s, box-shadow 0.2s, transform 0.1s;
        }
        input[type="submit"]:hover {
            background: linear-gradient(90deg, #1565c0 0%, #1976d2 100%);
            box-shadow: 0 4px 16px rgba(25, 118, 210, 0.18);
            transform: translateY(-2px) scale(1.03);
        }
        input[type="submit"]:active {
            background: #125ea2;
            box-shadow: 0 2px 8px rgba(25, 118, 210, 0.10);
            transform: scale(0.98);
        }
        input[type="submit"]:focus {
            outline: 2px solid #42a5f5;
            outline-offset: 2px;
        }
    </style>
</head>
<body>
<div class="container">
    <h2>Create a New Quiz</h2>
    <p>
        After creating your quiz, you will be able to add questions of various types:
    <ul>
        <li>Question-Response (short text answer)</li>
        <li>Fill in the Blank</li>
        <li>Multiple Choice</li>
        <li>Picture Response</li>
    </ul>
    </p>
    <form action="createQuiz" method="post">
        <input type="hidden" name="creator_id" value="<%= session.getAttribute("userId") %>">
        <label for="title">Quiz Title:</label><br>
        <input type="text" id="title" name="title" required><br>

        <label for="description">Description:</label><br>
        <textarea id="description" name="description" rows="4" cols="50" required></textarea><br>

        <input type="submit" value="Create Quiz">
    </form>
</div>
</body>
</html>