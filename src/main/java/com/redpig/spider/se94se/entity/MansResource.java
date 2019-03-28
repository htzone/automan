package com.redpig.spider.se94se.entity;

import com.redpig.common.entity.AuditableEntity;

/**
 * Created by hetao on 2018/6/25.
 */
public class MansResource extends AuditableEntity {

    private static final long serialVersionUID = -6006023610623395446L;
    private String title;
    private String description;
    private String picUrl;
    private String playLink;
    private String playPageUrl;
    private String category;
    private String webSite;

    public String getPlayPageUrl() {
        return playPageUrl;
    }

    public void setPlayPageUrl(String playPageUrl) {
        this.playPageUrl = playPageUrl;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPicUrl() {
        return picUrl;
    }

    public void setPicUrl(String picUrl) {
        this.picUrl = picUrl;
    }

    public String getPlayLink() {
        return playLink;
    }

    public void setPlayLink(String playLink) {
        this.playLink = playLink;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getWebSite() {
        return webSite;
    }

    public void setWebSite(String webSite) {
        this.webSite = webSite;
    }
}
