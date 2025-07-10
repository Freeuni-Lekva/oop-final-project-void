package quizAttempt;

public class QuizAttemptService {

    private final QuizAttemptRepository quizAttemptRepository;

    public QuizAttemptService(QuizAttemptRepository quizAttemptRepository) {
        this.quizAttemptRepository = quizAttemptRepository;
    }

    public QuizAttempt createQuizAttempt(QuizAttempt quizAttempt) {
        validateQuizAttempt(quizAttempt);
        quizAttemptRepository.create(quizAttempt);
        return quizAttempt;
    }

    private void validateQuizAttempt(QuizAttempt quizAttempt) {
        if (quizAttempt == null) {
            throw new IllegalArgumentException("Quiz attempt cannot be null");
        }

        if (quizAttempt.getQuizId() <= 0) {
            throw new IllegalArgumentException("Invalid quiz ID");
        }

        if (quizAttempt.getUserId() <= 0) {
            throw new IllegalArgumentException("Invalid user ID");
        }

        if (quizAttempt.getTotalQuestions() < 0) {
            throw new IllegalArgumentException("Total questions cannot be negative");
        }

        if (quizAttempt.getScore() < 0 || quizAttempt.getScore() > quizAttempt.getTotalQuestions()) {
            throw new IllegalArgumentException("Invalid score value");
        }

        if (quizAttempt.getTimeTaken() < 0) {
            throw new IllegalArgumentException("Time taken cannot be negative");
        }

        if (quizAttempt.getAttemptedAt() == null) {
            throw new IllegalArgumentException("Attempt date/time must be provided");
        }
    }
}
