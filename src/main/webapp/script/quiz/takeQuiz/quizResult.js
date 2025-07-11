document.addEventListener("DOMContentLoaded", () => {
    const resultDiv = document.getElementById("result");
    resultDiv.textContent = `Your Score: ${score} out of ${totalQuestions}`;

    const homeBtn = document.getElementById("home-btn");
    homeBtn.addEventListener("click", () => {
        window.location.href = "/";
    });
});
