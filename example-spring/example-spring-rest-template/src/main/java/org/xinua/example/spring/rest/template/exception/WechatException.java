package org.xinua.example.spring.rest.template.exception;


public class WechatException extends RuntimeException {

    private Integer errcode;//微信返回字段
    private String errmsg;//微信返回字段

    public WechatException() {
    }

    public WechatException(int errcode, String errmsg) {
        super(errmsg);
        this.errcode = errcode;
        this.errmsg = errmsg;
    }

    public WechatException(int errcode, String errmsg, String message) {
        super(message);
        this.errcode = errcode;
        this.errmsg = errmsg;
    }

    public int getErrcode() {
        return errcode;
    }

    public void setErrcode(int errcode) {
        this.errcode = errcode;
    }

    public String getErrmsg() {
        return errmsg;
    }

    public void setErrmsg(String errmsg) {
        this.errmsg = errmsg;
    }

}
