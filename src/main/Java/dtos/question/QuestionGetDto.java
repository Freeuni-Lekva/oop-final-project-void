package dtos.question;

public class QuestionGetDto {
    private int questionId;
    private int quizId;
    private String questionText;
    private String type;
    private String imageUrl;
    private int questionOrder;

    public QuestionGetDto(int questionId, int quizId, String questionText, String type, String imageUrl, int questionOrder) {
        this.questionId = questionId;
        this.quizId = quizId;
        this.questionText = questionText;
        this.type = type;
        this.imageUrl = imageUrl;
        this.questionOrder = questionOrder;
    }

    public int getQuestionId() { return questionId; }
    public int getQuizId() { return quizId; }
    public String getQuestionText() { return questionText; }
    public String getType() { return type; }
    public String getImageUrl() { return imageUrl; }
    public int getQuestionOrder() { return questionOrder; }
} 