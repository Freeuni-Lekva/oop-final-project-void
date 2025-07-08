<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Quiz Created</title>
    <style>
        body {
            background: #f4f6f8;
            font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
            margin: 0;
            padding: 0;
        }
        .container {
            max-width: 420px;
            margin: 80px auto;
            background: #fff;
            border-radius: 12px;
            box-shadow: 0 4px 24px rgba(0,0,0,0.08);
            padding: 32px 28px 24px 28px;
            text-align: center;
        }
        h2 {
            color: #2d3a4b;
            margin-bottom: 18px;
        }
        .btn {
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
            margin-top: 18px;
        }
        .btn:hover {
            background: #125ea2;
        }
    </style>
</head>
<body>
<div class="container">
    <h2>Quiz Successfully Created!</h2>
    <p>Your quiz has been saved. You can now return to the homepage or create another quiz.</p>
    <form action="createQuiz" method="get">
        <input type="submit" value="Go to Homepage" class="btn">
    </form>
</div>
</body>
</html> 