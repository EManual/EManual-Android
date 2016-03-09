package io.github.emanual.app.entity;

/**
 * Author: jayin
 * Date: 2/25/16
 */
public class InterviewJSONEntity extends BaseEntity {

    private String title;
    private InterviewInfoEntity info;

    public InterviewInfoEntity getInfo() {
        return info;
    }

    public void setInfo(InterviewInfoEntity info) {
        this.info = info;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Override public String toString() {
        return "InterviewJSONEntity{" +
                "info=" + info +
                ", title='" + title + '\'' +
                '}';
    }
}
