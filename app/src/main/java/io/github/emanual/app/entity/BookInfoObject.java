package io.github.emanual.app.entity;

import java.io.Serializable;
import java.util.List;

/**
 * Book: info.json 参考 https://github.com/EManualResource/feeds-book/blob/gh-pages/feeds/all.json#L2-L16
 * Author: jayin
 * Date: 1/23/16
 */
public class BookInfoObject implements Serializable{
    private String name;
    private String name_cn;
    private String md5;
    private String icon_url;
    private String url;
    private String author;
    private String homepage;
    private List<MaintainerObject> maintainers;

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

    public List<MaintainerObject> getMaintainers() {
        return maintainers;
    }

    public void setMaintainers(List<MaintainerObject> maintainers) {
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
        return "BookInfoObject{" +
                "author='" + author + '\'' +
                ", name='" + name + '\'' +
                ", name_cn='" + name_cn + '\'' +
                ", md5='" + md5 + '\'' +
                ", icon_url='" + icon_url + '\'' +
                ", url='" + url + '\'' +
                ", homepage='" + homepage + '\'' +
                ", maintainers=" + maintainers +
                '}';
    }
}
