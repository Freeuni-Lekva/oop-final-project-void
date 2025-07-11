const urlParams = new URLSearchParams(window.location.search);
const quizId = urlParams.get("id");

if (!quizId) {
    alert("No quiz ID provided.");
} else {
    fetch(`/quiz/info?id=${quizId}`)
        .then(res => {
            if (!res.ok) throw new Error("Quiz not found");
            return res.json();
        })
        .then(data => {
            document.getElementById("quiz-title").textContent = data.title;
            document.getElementById("quiz-description").textContent = data.description;
        })
        .catch(err => {
            alert("Failed to load quiz info: " + err.message);
        });

    document.getElementById("startQuizBtn").addEventListener("click", function () {
        window.location.href = `/quiz/take?id=${quizId}`;
    });
}