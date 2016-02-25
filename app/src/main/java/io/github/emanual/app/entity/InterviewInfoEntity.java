package io.github.emanual.app.entity;

import java.util.List;

/**
 * Author: jayin
 * Date: 2/25/16
 */
public class InterviewInfoEntity extends BaseEntity{

    private String name;
    private String name_cn;
    private String md5;
    private String icon_url;
    private String url;
    private String author;
    private String homepage;
    private List<MaintainerEntity> maintainers;

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getHomepage() {
        return homepage;
    }

    public void setHomepage(String homepage) {
        this.homepage = homepage;
    }

    public String getIcon_url() {
        return icon_url;
    }

    public void setIcon_url(String icon_url) {
        this.icon_url = icon_url;
    }

    public List<MaintainerEntity> getMaintainers() {
        return maintainers;
    }

    public void setMaintainers(List<MaintainerEntity> maintainers) {
        this.maintainers = maintainers;
    }

    public String getMd5() {
        return md5;
    }

    public void setMd5(String md5) {
        this.md5 = md5;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName_cn() {
        return name_cn;
    }

    public void setName_cn(String name_cn) {
        this.name_cn = name_cn;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    @Override public String toString() {
        return "InterviewInfoEntity{" +
                "author='" + author + '\'' +
                ", homepage='" + homepage + '\'' +
                ", icon_url='" + icon_url + '\'' +
                ", maintainers=" + maintainers +
                ", md5='" + md5 + '\'' +
                ", name='" + name + '\'' +
                ", name_cn='" + name_cn + '\'' +
                ", url='" + url + '\'' +
                '}';
    }
}
