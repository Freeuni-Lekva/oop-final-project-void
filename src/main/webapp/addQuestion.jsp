<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Add Question</title>
    <style>
        body {
            background: linear-gradient(120deg, #e0e7ff 0%, #f3e8ff 100%);
            min-height: 100vh;
            font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
            margin: 0;
            padding: 0;
        }
        .container {
            max-width: 500px;
            margin: 50px auto;
            background: #fff;
            border-radius: 16px;
            border: 1.5px solid #e3e8ee;
            box-shadow: 0 6px 32px rgba(0,0,0,0.10);
            padding: 44px 36px 32px 36px;
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
            padding: 12px 14px;
            margin-top: 4px;
            margin-bottom: 18px;
            border: 1.5px solid #cfd8dc;
            border-radius: 8px;
            font-size: 16px;
            background: #f9fafb;
            transition: border 0.2s, box-shadow 0.2s;
            box-shadow: 0 1px 4px rgba(60, 80, 180, 0.06);
        }
        input[type="text"]:focus, textarea:focus, select:focus {
            border: 1.5px solid #1976d2;
            outline: none;
            box-shadow: 0 2px 8px rgba(25, 118, 210, 0.10);
        }
        input[type="submit"], .finish-btn {
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
            margin-top: 8px;
        }
        input[type="submit"]:hover, .finish-btn:hover {
            background: linear-gradient(90deg, #1565c0 0%, #1976d2 100%);
            box-shadow: 0 4px 16px rgba(25, 118, 210, 0.18);
            transform: translateY(-2px) scale(1.03);
        }
        input[type="submit"]:active, .finish-btn:active {
            background: #125ea2;
            box-shadow: 0 2px 8px rgba(25, 118, 210, 0.10);
            transform: scale(0.98);
        }
        input[type="submit"]:focus, .finish-btn:focus {
            outline: 2px solid #42a5f5;
            outline-offset: 2px;
        }
        .type-section {
            display: none;
        }
        .type-section label {
            margin-top: 8px;
        }
        /* Multiple Choice Custom Styling */
        .mc-choices {
            display: flex;
            flex-direction: column;
            gap: 14px;
            margin-bottom: 18px;
        }
        .mc-choice-label {
            display: flex;
            align-items: center;
            background: #f4f8ff;
            border: 1.5px solid #cfd8dc;
            border-radius: 8px;
            padding: 10px 14px;
            font-size: 16px;
            cursor: pointer;
            transition: border 0.2s, background 0.2s, box-shadow 0.2s;
            box-shadow: 0 1px 4px rgba(60, 80, 180, 0.06);
            position: relative;
        }
        .mc-choice-label:hover {
            border: 1.5px solid #1976d2;
            background: #e3f0ff;
        }
        .mc-choice-radio {
            position: absolute;
            opacity: 0;
            pointer-events: none;
        }
        .mc-choice-radio:checked + .mc-choice-custom {
            border: 2.5px solid #1976d2;
            background: #e3f0ff;
        }
        .mc-choice-custom {
            width: 20px;
            height: 20px;
            border-radius: 50%;
            border: 2px solid #cfd8dc;
            margin-right: 12px;
            background: #fff;
            display: flex;
            align-items: center;
            justify-content: center;
            transition: border 0.2s, background 0.2s;
        }
        .mc-choice-radio:checked + .mc-choice-custom::after {
            content: '';
            display: block;
            width: 10px;
            height: 10px;
            border-radius: 50%;
            background: #1976d2;
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
<%
    String nextOrderParam = request.getParameter("nextOrder");
    int questionOrder = 1;
    if (nextOrderParam != null) {
        try {
            questionOrder = Integer.parseInt(nextOrderParam);
        } catch (NumberFormatException e) {
            questionOrder = 1;
        }
    }
%>
<div class="container">
    <h2>Add a Question</h2>
    <% String errorMessage = (String) request.getAttribute("errorMessage"); %>
    <% if (errorMessage != null) { %>
    <div style="color: #b71c1c; background: #ffebee; border: 1px solid #ef9a9a; padding: 10px; border-radius: 6px; margin-bottom: 16px;">
        <%= errorMessage %>
    </div>
    <% } %>
    <form action="addQuestion" method="post">
        <input type="hidden" name="quizId" value="<%= request.getParameter("quizId") %>">
        <input type="hidden" name="question_order" value="<%= questionOrder %>">

        <label for="questionType">Question Type:</label>
        <select id="questionType" name="questionType" onchange="showTypeSection()">
            <option value="question_response">Question-Response</option>
            <option value="fill_blank">Fill in the Blank</option>
            <option value="multiple_choice">Multiple Choice</option>
            <option value="picture_response">Picture Response</option>
        </select>


        <div class="type-section" id="question_response-section" style="display:block;">
            <label for="qr-question">Question:</label>
            <input type="text" id="qr-question" name="qr-question">
            <label for="qr-answer">Answer:</label>
            <input type="text" id="qr-answer" name="qr-answer">
        </div>


        <div class="type-section" id="fill_blank-section">
            <label for="fb-question">Question (use ___ for the blank):</label>
            <input type="text" id="fb-question" name="fb-question">
            <label for="fb-answer">Answer:</label>
            <input type="text" id="fb-answer" name="fb-answer">
        </div>


        <div class="type-section" id="multiple_choice-section">
            <label for="mc-question">Question:</label>
            <input type="text" id="mc-question" name="mc-question">
            <label>Choices (mark the correct one):</label><br>
            <div class="mc-choices">
                <label class="mc-choice-label">
                    <input type="radio" class="mc-choice-radio" name="mc-correct" value="1" checked>
                    <span class="mc-choice-custom"></span>
                    <input type="text" name="mc-choice1" placeholder="Choice 1" style="margin-bottom:0; margin-right:10px; flex:1;">
                    <span style="margin-left:8px; color:#1976d2; font-weight:600;">Correct</span>
                </label>
                <label class="mc-choice-label">
                    <input type="radio" class="mc-choice-radio" name="mc-correct" value="2">
                    <span class="mc-choice-custom"></span>
                    <input type="text" name="mc-choice2" placeholder="Choice 2" style="margin-bottom:0; margin-right:10px; flex:1;">
                    <span style="margin-left:8px; color:#1976d2; font-weight:600;">Correct</span>
                </label>
                <label class="mc-choice-label">
                    <input type="radio" class="mc-choice-radio" name="mc-correct" value="3">
                    <span class="mc-choice-custom"></span>
                    <input type="text" name="mc-choice3" placeholder="Choice 3" style="margin-bottom:0; margin-right:10px; flex:1;">
                    <span style="margin-left:8px; color:#1976d2; font-weight:600;">Correct</span>
                </label>
                <label class="mc-choice-label">
                    <input type="radio" class="mc-choice-radio" name="mc-correct" value="4">
                    <span class="mc-choice-custom"></span>
                    <input type="text" name="mc-choice4" placeholder="Choice 4" style="margin-bottom:0; margin-right:10px; flex:1;">
                    <span style="margin-left:8px; color:#1976d2; font-weight:600;">Correct</span>
                </label>
            </div>
        </div>


        <div class="type-section" id="picture_response-section">
            <label for="pr-question">Question:</label>
            <input type="text" id="pr-question" name="pr-question">
            <label for="pr-imageUrl">Image URL:</label>
            <input type="text" id="pr-imageUrl" name="pr-imageUrl">
            <label for="pr-answer">Answer:</label>
            <input type="text" id="pr-answer" name="pr-answer">
        </div>

        <input type="submit" value="Add Question">
    </form>
    <form action="finishQuiz" method="post" style="margin-top: 10px;">
        <input type="submit" value="Finish Quiz" class="finish-btn">
    </form>
</div>
<script>
    showTypeSection();
</script>
</body>
</html>