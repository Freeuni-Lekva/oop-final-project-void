<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Create New Quiz</title>
    <style>
        body {
            background: #f4f6f8;
            font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
            margin: 0;
            padding: 0;
        }
        .container {
            max-width: 420px;
            margin: 50px auto;
            background: #fff;
            border-radius: 12px;
            box-shadow: 0 4px 24px rgba(0,0,0,0.08);
            padding: 32px 28px 24px 28px;
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
            background: #1976d2;
            color: #fff;
            border: none;
            border-radius: 6px;
            padding: 10px 0;
            width: 100%;
            font-size: 16px;
            font-weight: 600;
            cursor: pointer;
            transition: background 0.2s;
        }
        input[type="submit"]:hover {
            background: #125ea2;
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