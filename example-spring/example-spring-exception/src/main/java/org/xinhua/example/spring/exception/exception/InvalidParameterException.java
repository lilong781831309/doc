package org.xinhua.example.spring.exception.exception;


public class InvalidParameterException extends BusinessException {
    private static final long serialVersionUID = 3592879054970585260L;

    public InvalidParameterException(int errCode, String errMsg) {
        super(errCode, errMsg);
    }

    public InvalidParameterException(int errCode, String errMsg, Object... args) {
        super(errCode, errMsg, args);
    }

    public InvalidParameterException(int errCode, String errMsg, Throwable cause) {
        super(errCode, errMsg, cause);
    }

    public InvalidParameterException(int errCode, String errMsg, Throwable cause, Object... args) {
        super(errCode, errMsg, cause, args);
    }
}
