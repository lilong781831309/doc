package org.xinhua.example.spring.exception.exception;

public class AuthenticationException extends BusinessException {
    private static final long serialVersionUID = -8047957886929831355L;

    public AuthenticationException(int errCode, String errMsg) {
        super(errCode, errMsg);
    }

    public AuthenticationException(int errCode, String errMsg, Object... args) {
        super(errCode, errMsg, args);
    }

    public AuthenticationException(int errCode, String errMsg, Throwable cause) {
        super(errCode, errMsg, cause);
    }

    public AuthenticationException(int errCode, String errMsg, Throwable cause, Object... args) {
        super(errCode, errMsg, cause, args);
    }
}
