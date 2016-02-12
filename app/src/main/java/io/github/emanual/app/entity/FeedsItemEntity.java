package io.github.emanual.app.entity;

import java.io.Serializable;

/**
 * Author: jayin
 * Date: 1/19/16
 */
public class FeedsItemEntity extends BaseEntity implements Serializable{
    private String name;
    private String name_cn;
    private String md5;
    private String icon_url;
    private String url;
    private String author;
    private String homepage;

    /**
     * 获取book的下载url
     * NOTE:
     *  在url上加上`?v=xxxx`
     * @return
     */
    public String getDownloadUrl(){
        return String.format("%s?v=%s", getUrl(), getMd5());
    }

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
        return "FeedsItemEntity{" +
                "author='" + author + '\'' +
                ", name='" + name + '\'' +
                ", name_cn='" + name_cn + '\'' +
                ", md5='" + md5 + '\'' +
                ", icon_url='" + icon_url + '\'' +
                ", url='" + url + '\'' +
                ", homepage='" + homepage + '\'' +
                '}';
    }
}
