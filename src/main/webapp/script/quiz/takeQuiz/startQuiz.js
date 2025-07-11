document.getElementById("startQuizBtn").addEventListener("click", function () {
    const urlParams = new URLSearchParams(window.location.search);
    const quizId = urlParams.get("id");

    if (!quizId) {
        alert("No quiz ID found!");
        return;
    }
    window.location.href = `/quiz/take?id=${quizId}`;
});
