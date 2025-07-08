<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Quiz Created</title>
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
            margin: 80px auto;
            background: #fff;
            border-radius: 16px;
            border: 1.5px solid #e3e8ee;
            box-shadow: 0 6px 32px rgba(0,0,0,0.10);
            padding: 40px 32px 28px 32px;
            text-align: center;
        }
        h2 {
            color: #2d3a4b;
            margin-bottom: 18px;
        }
        .btn {
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
            margin-top: 18px;
        }
        .btn:hover {
            background: linear-gradient(90deg, #1565c0 0%, #1976d2 100%);
            box-shadow: 0 4px 16px rgba(25, 118, 210, 0.18);
            transform: translateY(-2px) scale(1.03);
        }
        .btn:active {
            background: #125ea2;
            box-shadow: 0 2px 8px rgba(25, 118, 210, 0.10);
            transform: scale(0.98);
        }
        .btn:focus {
            outline: 2px solid #42a5f5;
            outline-offset: 2px;
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