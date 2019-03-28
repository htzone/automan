package com.redpig.spider.paxy.entity;

import java.io.Serializable;

/**
 * Created by hetao on 2018/6/20.
 */
public class ArticleInfo implements Serializable{
    private static final long serialVersionUID = 1856344655202334769L;
    private String createTime;
    private String fileTitle;
    private String creatorCode;
    private String clickCount;
    private String fileCode;
    private String tag;
    private String fileExpandCode;
    private String pushSummary;
    private String htmlFilterImgUrl;
    private String fileContent;
    private String htmlUrl;
    private String creatorName;
    private String inscribe;
    private String thumbnailUrl;

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getFileTitle() {
        return fileTitle;
    }

    public void setFileTitle(String fileTitle) {
        this.fileTitle = fileTitle;
    }

    public String getCreatorCode() {
        return creatorCode;
    }

    public void setCreatorCode(String creatorCode) {
        this.creatorCode = creatorCode;
    }

    public String getClickCount() {
        return clickCount;
    }

    public void setClickCount(String clickCount) {
        this.clickCount = clickCount;
    }

    public String getFileCode() {
        return fileCode;
    }

    public void setFileCode(String fileCode) {
        this.fileCode = fileCode;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public String getFileExpandCode() {
        return fileExpandCode;
    }

    public void setFileExpandCode(String fileExpandCode) {
        this.fileExpandCode = fileExpandCode;
    }

    public String getPushSummary() {
        return pushSummary;
    }

    public void setPushSummary(String pushSummary) {
        this.pushSummary = pushSummary;
    }

    public String getHtmlFilterImgUrl() {
        return htmlFilterImgUrl;
    }

    public void setHtmlFilterImgUrl(String htmlFilterImgUrl) {
        this.htmlFilterImgUrl = htmlFilterImgUrl;
    }

    public String getFileContent() {
        return fileContent;
    }

    public void setFileContent(String fileContent) {
        this.fileContent = fileContent;
    }

    public String getHtmlUrl() {
        return htmlUrl;
    }

    public void setHtmlUrl(String htmlUrl) {
        this.htmlUrl = htmlUrl;
    }

    public String getCreatorName() {
        return creatorName;
    }

    public void setCreatorName(String creatorName) {
        this.creatorName = creatorName;
    }

    public String getInscribe() {
        return inscribe;
    }

    public void setInscribe(String inscribe) {
        this.inscribe = inscribe;
    }

    public String getThumbnailUrl() {
        return thumbnailUrl;
    }

    public void setThumbnailUrl(String thumbnailUrl) {
        this.thumbnailUrl = thumbnailUrl;
    }
}
