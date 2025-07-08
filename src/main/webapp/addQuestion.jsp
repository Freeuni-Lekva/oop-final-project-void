<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Add Question</title>
    <style>
        body {
            background: #f4f6f8;
            font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
            margin: 0;
            padding: 0;
        }
        .container {
            max-width: 480px;
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
        label {
            font-weight: 500;
            color: #2d3a4b;
        }
        input[type="text"], textarea, select {
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
        input[type="text"]:focus, textarea:focus, select:focus {
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
        .type-section {
            display: none;
        }
        .type-section label {
            margin-top: 8px;
        }
    </style>
    <script>
        function showTypeSection() {
            const type = document.getElementById('questionType').value;
            const sections = document.querySelectorAll('.type-section');
            sections.forEach(function(sec) { sec.style.display = 'none'; });
            document.getElementById(type + '-section').style.display = 'block';
        }
    </script>
</head>
<body>
<div class="container">
    <h2>Add a Question</h2>
    <form action="addQuestion" method="post">
        <input type="hidden" name="quizId" value="<%= request.getParameter("quizId") %>">

        <label for="questionType">Question Type:</label>
        <select id="questionType" name="questionType" onchange="showTypeSection()">
            <option value="questionResponse">Question-Response</option>
            <option value="fillBlank">Fill in the Blank</option>
            <option value="multipleChoice">Multiple Choice</option>
            <option value="pictureResponse">Picture Response</option>
        </select>


        <div class="type-section" id="questionResponse-section" style="display:block;">
            <label for="qr-question">Question:</label>
            <input type="text" id="qr-question" name="qr-question">
            <label for="qr-answer">Answer:</label>
            <input type="text" id="qr-answer" name="qr-answer">
        </div>


        <div class="type-section" id="fillBlank-section">
            <label for="fb-question">Question (use ___ for the blank):</label>
            <input type="text" id="fb-question" name="fb-question">
            <label for="fb-answer">Answer:</label>
            <input type="text" id="fb-answer" name="fb-answer">
        </div>


        <div class="type-section" id="multipleChoice-section">
            <label for="mc-question">Question:</label>
            <input type="text" id="mc-question" name="mc-question">
            <label>Choices (mark the correct one):</label><br>
            <input type="text" name="mc-choice1" placeholder="Choice 1">
            <input type="radio" name="mc-correct" value="1" checked> Correct<br>
            <input type="text" name="mc-choice2" placeholder="Choice 2">
            <input type="radio" name="mc-correct" value="2"> Correct<br>
            <input type="text" name="mc-choice3" placeholder="Choice 3">
            <input type="radio" name="mc-correct" value="3"> Correct<br>
            <input type="text" name="mc-choice4" placeholder="Choice 4">
            <input type="radio" name="mc-correct" value="4"> Correct<br>
        </div>


        <div class="type-section" id="pictureResponse-section">
            <label for="pr-question">Question:</label>
            <input type="text" id="pr-question" name="pr-question">
            <label for="pr-imageUrl">Image URL:</label>
            <input type="text" id="pr-imageUrl" name="pr-imageUrl">
            <label for="pr-answer">Answer:</label>
            <input type="text" id="pr-answer" name="pr-answer">
        </div>

        <input type="submit" value="Add Question">
    </form>
    <form action="quizSuccess.jsp" method="get" style="margin-top: 10px;">
        <input type="hidden" name="quizId" value="<%= request.getParameter("quizId") %>">
        <input type="submit" value="Finish Quiz" style="background: #43a047; color: #fff; font-weight: 600; border: none; border-radius: 6px; padding: 10px 0; width: 100%; font-size: 16px; margin-top: 8px; cursor: pointer; transition: background 0.2s;">
    </form>
</div>
<script>
    showTypeSection();
</script>
</body>
</html> 