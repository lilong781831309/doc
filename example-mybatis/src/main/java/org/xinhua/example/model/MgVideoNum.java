package org.xinhua.example.model;

import java.util.Date;

public class MgVideoNum {
    private Long id;

    private Long pid;

    private Long videoId;

    private Integer videoNum;

    private Long mediaId;

    private String mp4Url;

    private Date expiresTime;

    private Integer isDel;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getPid() {
        return pid;
    }

    public void setPid(Long pid) {
        this.pid = pid;
    }

    public Long getVideoId() {
        return videoId;
    }

    public void setVideoId(Long videoId) {
        this.videoId = videoId;
    }

    public Integer getVideoNum() {
        return videoNum;
    }

    public void setVideoNum(Integer videoNum) {
        this.videoNum = videoNum;
    }

    public Long getMediaId() {
        return mediaId;
    }

    public void setMediaId(Long mediaId) {
        this.mediaId = mediaId;
    }

    public String getMp4Url() {
        return mp4Url;
    }

    public void setMp4Url(String mp4Url) {
        this.mp4Url = mp4Url == null ? null : mp4Url.trim();
    }

    public Date getExpiresTime() {
        return expiresTime;
    }

    public void setExpiresTime(Date expiresTime) {
        this.expiresTime = expiresTime;
    }

    public Integer getIsDel() {
        return isDel;
    }

    public void setIsDel(Integer isDel) {
        this.isDel = isDel;
    }
}