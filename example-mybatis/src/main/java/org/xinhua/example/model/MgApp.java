package org.xinhua.example.model;

import java.util.Date;

public class MgApp {
    private Long pid;

    private String appId;

    private String appSecret;

    private String accessToken;

    private Date expiresTime;

    public Long getPid() {
        return pid;
    }

    public void setPid(Long pid) {
        this.pid = pid;
    }

    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId == null ? null : appId.trim();
    }

    public String getAppSecret() {
        return appSecret;
    }

    public void setAppSecret(String appSecret) {
        this.appSecret = appSecret == null ? null : appSecret.trim();
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken == null ? null : accessToken.trim();
    }

    public Date getExpiresTime() {
        return expiresTime;
    }

    public void setExpiresTime(Date expiresTime) {
        this.expiresTime = expiresTime;
    }
}