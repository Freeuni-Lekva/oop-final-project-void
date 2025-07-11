document.addEventListener("DOMContentLoaded", () => {
    //getting quiz id
    const urlParams = new URLSearchParams(window.location.search);
    const quizId = urlParams.get("id");

    if (!quizId) {
        alert("No quiz ID found in the URL");
        return;
    }

    // requesting quiz data from data servlet
    fetch(`/quiz/take/data?id=${quizId}`)
        .then(response => response.json())
        .then(quiz => {
            window.currentQuizId = quizId;
            showQuiz(quiz);
        })
        .catch(error => {
            alert("Error loading quiz: " + error.message);
        });
});

let currentQuestionIndex = 0;
let totalQuestions = 0;

//dynamically adding question to html skeleton
function showQuiz(quiz) {
    const quizForm = document.getElementById("quiz-form");
    const sidebar = document.getElementById("question-nav");
    const questions = quiz.questions;
    totalQuestions = questions.length;

    questions.forEach((question, index) => {
        const li = document.createElement("li");
        li.textContent = `${index + 1}`;
        li.dataset.index = index;
        li.onclick = () => {
            currentQuestionIndex = index;
            updateVisibleQuestion();
        };
        sidebar.appendChild(li);

        const questionDiv = document.createElement("div");
        questionDiv.className = "question";
        questionDiv.dataset.index = index;

        const p = document.createElement("p");
        p.textContent = `${index + 1}. ${question.question_text}`;
        questionDiv.appendChild(p);

        const name = `q${index + 1}`;
        const type = question.type;

        if (type === "multiple_choice") {
            question.choices.forEach(choice => {
                const label = document.createElement("label");
                const input = document.createElement("input");
                input.type = "radio";
                input.name = name;
                input.value = choice.choice_text;
                //we are adding class to it adn storing questions id, so we can retrieve
                //evrything after submitting
                input.classList.add("user-answer");
                input.dataset.questionId = question.question_id;

                label.appendChild(input);
                label.append(" " + choice.choice_text);
                questionDiv.appendChild(label);
                questionDiv.appendChild(document.createElement("br"));
            });
        } else if (
            type === "fill_blank" ||
            type === "question_response" ||
            type === "picture_response"
        ) {
            const input = document.createElement("input");
            input.type = "text";
            input.name = name;
            input.classList.add("user-answer");
            input.dataset.questionId = question.question_id;

            if (type === "picture_response") {
                const img = document.createElement("img");
                img.src = question.image_url;
                // img.src = "/css/images/startQuizBG.png";
                img.alt = "Picture";
                img.className = "question-image";
                questionDiv.appendChild(img);
            }

            questionDiv.appendChild(input);
        }


        if (index !== 0) {
            questionDiv.style.display = "none";
        }

        quizForm.insertBefore(questionDiv, document.querySelector(".navigation-buttons"));
        window.quizStartTime = Date.now();
    });

    document.getElementById("prevBtn").onclick = () => {
        if (currentQuestionIndex > 0) {
            currentQuestionIndex--;
            updateVisibleQuestion();
        }
    };

    document.getElementById("nextBtn").onclick = () => {
        if (currentQuestionIndex < totalQuestions - 1) {
            currentQuestionIndex++;
            updateVisibleQuestion();
        }
    };

    updateVisibleQuestion();
}


function updateVisibleQuestion() {
    const allQuestions = document.querySelectorAll(".question");
    const allSidebarItems = document.querySelectorAll("#question-nav li");

    allQuestions.forEach((question, index) => {
        question.style.display = index === currentQuestionIndex ? "block" : "none";
    });

    allSidebarItems.forEach((item, index) => {
        if (index === currentQuestionIndex) {
            item.classList.add("active");
        } else {
            item.classList.remove("active");
        }
    });

    const prevBtn = document.getElementById("prevBtn");
    const nextBtn = document.getElementById("nextBtn");
    const finishBtn = document.getElementById("finishBtn");

    prevBtn.disabled = currentQuestionIndex === 0;
    nextBtn.style.display = currentQuestionIndex === totalQuestions - 1 ? "none" : "inline-block";
    finishBtn.style.display = currentQuestionIndex === totalQuestions - 1 ? "inline-block" : "none";
}


document.getElementById("finishBtn").addEventListener("click", function (e) {
    e.preventDefault();

    const endTime = Date.now();
    const timeTakenSeconds = Math.floor((endTime - window.quizStartTime) / 1000);

    const answersMap = {};

    //collect all answer elements
    document.querySelectorAll(".user-answer").forEach(el => {
        const questionId = parseInt(el.dataset.questionId);
        if (!questionId) return;

        if (!(questionId in answersMap)) {
            answersMap[questionId] = null;
        }
        if(el.value === null) return;
        if (el.type === "radio" && el.checked) {
            answersMap[questionId] = el.value;
        } else if (el.type !== "radio") {
            const value = el.value.trim();
            if (value) {
                answersMap[questionId] = value;
            }
        }
    });

    //onvert map to array of { questionId, answerText }
    const answers = Object.entries(answersMap).map(([questionId, answerText]) => ({
        questionId: parseInt(questionId),
        answerText
    }));

    const payload = {
        totalQuestions: totalQuestions,
        attemptedAt: new Date(window.quizStartTime).toISOString(),
        timeTaken: timeTakenSeconds,
        quizId: window.currentQuizId,
        answers: answers
    };

    fetch("/quiz/submit", {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify(payload)
    })
        .then(res => {
            if (res.ok) {
                alert("Quiz submitted successfully!");
                window.location.href = "/quiz/submit";
            } else {
                alert("Failed to submit quiz.");
            }
        })
        .catch(err => alert("Error submitting: " + err.message));
});
