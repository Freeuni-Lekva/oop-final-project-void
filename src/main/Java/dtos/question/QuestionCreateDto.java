package dtos.question;

public class QuestionCreateDto {
    private int quizId;
    private String questionText;
    private String type;
    private String imageUrl;
    private int questionOrder;
    private String answer;
    private String[] choices;
    private int correctIndex;

    public QuestionCreateDto(int quizId, String questionText, String type, String imageUrl, int questionOrder, String answer, String[] choices, int correctIndex) {
        this.quizId = quizId;
        this.questionText = questionText;
        this.type = type;
        this.imageUrl = imageUrl;
        this.questionOrder = questionOrder;
        this.answer = answer;
        this.choices = choices;
        this.correctIndex = correctIndex;
    }

    public int getQuizId() { return quizId; }
    public String getQuestionText() { return questionText; }
    public String getType() { return type; }
    public String getImageUrl() { return imageUrl; }
    public int getQuestionOrder() { return questionOrder; }
    public String getAnswer() { return answer; }
    public String[] getChoices() { return choices; }
    public int getCorrectIndex() { return correctIndex; }
} 