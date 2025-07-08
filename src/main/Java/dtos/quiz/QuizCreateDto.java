package dtos.quiz;

public class QuizCreateDto {
    private String title;
    private String description;
    private long creatorId;
    private Boolean randomize;
    private Boolean isOnePage;
    private Boolean immediateCorrection;
    private Boolean practiceMode;

    public QuizCreateDto(String title, String description, long creatorId, Boolean randomize, Boolean isOnePage, Boolean immediateCorrection, Boolean practiceMode) {
        this.title = title;
        this.description = description;
        this.creatorId = creatorId;
        this.randomize = randomize;
        this.isOnePage = isOnePage;
        this.immediateCorrection = immediateCorrection;
        this.practiceMode = practiceMode;
    }

    public String getTitle() { return title; }
    public String getDescription() { return description; }
    public long getCreatorId() { return creatorId; }
    public Boolean getRandomize() { return randomize; }
    public Boolean getIsOnePage() { return isOnePage; }
    public Boolean getImmediateCorrection() { return immediateCorrection; }
    public Boolean getPracticeMode() { return practiceMode; }
} 