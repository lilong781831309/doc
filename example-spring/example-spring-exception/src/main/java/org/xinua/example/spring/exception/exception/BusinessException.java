package org.xinua.example.spring.exception.exception;

public class BusinessException extends RuntimeException {
    private static final long serialVersionUID = 7395352602270638728L;

    private int errCode;
    private String errMsg;
    private Object[] args;

    public BusinessException(int errCode, String errMsg) {
        super(errMsg);
        this.errCode = errCode;
        this.errMsg = errMsg;
    }

    public BusinessException(int errCode, String errMsg, Object... args) {
        super(errMsg);
        this.errCode = errCode;
        this.errMsg = errMsg;
        this.args = args;
    }

    public BusinessException(int errCode, String errMsg, Throwable cause) {
        super(errMsg,cause);
        this.errCode = errCode;
        this.errMsg = errMsg;
    }

    public BusinessException(int errCode, String errMsg, Throwable cause, Object... args) {
        super(errMsg, cause);
        this.errCode = errCode;
        this.errMsg = errMsg;
        this.args = args;
    }

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

    public Object[] getArgs() {
        return args;
    }

    public void setArgs(String[] args) {
        this.args = args;
    }
}
