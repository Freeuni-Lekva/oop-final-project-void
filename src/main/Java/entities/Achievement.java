package entities;

public class Achievement {
    private Integer achievement_id;
    private String name;
    private String description;
    private String icon_url;

    public Achievement(Integer achievement_id, String name, String description, String icon_url) {
        this.achievement_id = achievement_id;
        this.name = name;
        this.description = description;
        this.icon_url = icon_url;
    }

    public Integer getAchievement_id() {
        return achievement_id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public String getIcon_url() {
        return icon_url;
    }

    public void setAchievement_id(Integer achievement_id) {
        this.achievement_id = achievement_id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setIcon_url(String icon_url) {
        this.icon_url = icon_url;
    }
}
