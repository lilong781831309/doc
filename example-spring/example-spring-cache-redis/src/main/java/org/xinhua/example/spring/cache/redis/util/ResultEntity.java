package org.xinhua.example.spring.cache.redis.util;


import java.io.Serializable;

public class ResultEntity implements Serializable {
    private static final long serialVersionUID = 1L;
    private int errCode = 0;
    private String errMsg = "";
    private String msg = "";
    private Object content;

    public int getErrCode() {
        return errCode;
    }

    public void setErrCode(int errCode) {
        this.errCode = errCode;
    }

    public String getErrMsg() {
        return errMsg;
    }

    public void setErrMsg(String errMsg) {
        this.errMsg = errMsg;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public Object getContent() {
        return content;
    }

    public void setContent(Object content) {
        this.content = content;
    }
}
