package io.github.emanual.app.entity;

import java.util.List;

/**
 * Author: jayin
 * Date: 2/26/16
 */
public class QuestionEntity extends BaseEntity {
    /**
     * 回答
     */
    private final static String TYPE_REPLY = "reply";
    /**
     * 选择
     */
    private final static String TYPE_CHOICE = "choice";
    /**
     * 判断
     */
    private final static String TYPE_JUDGMENT = "judgment";
    private String type;
    private String tag;
    private int difficulty;
    private String from;
    private String description;
    private String answer;
    private List<String> options;

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getDifficulty() {
        return difficulty;
    }

    public void setDifficulty(int difficulty) {
        this.difficulty = difficulty;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public List<String> getOptions() {
        return options;
    }

    public void setOptions(List<String> options) {
        this.options = options;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Override public String toString() {
        return "QuestionEntity{" +
                "answer='" + answer + '\'' +
                ", type='" + type + '\'' +
                ", tag='" + tag + '\'' +
                ", difficulty=" + difficulty +
                ", from='" + from + '\'' +
                ", description='" + description + '\'' +
                ", options=" + options +
                '}';
    }
}
