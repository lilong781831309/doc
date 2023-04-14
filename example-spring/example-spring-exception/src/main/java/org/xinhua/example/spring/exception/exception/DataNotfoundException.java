package org.xinhua.example.spring.exception.exception;

public class DataNotfoundException extends BusinessException {
    private static final long serialVersionUID = 7715465969822396855L;

    public DataNotfoundException(int errCode, String errMsg) {
        super(errCode, errMsg);
    }

    public DataNotfoundException(int errCode, String errMsg, Object... args) {
        super(errCode, errMsg, args);
    }

    public DataNotfoundException(int errCode, String errMsg, Throwable cause) {
        super(errCode, errMsg, cause);
    }

    public DataNotfoundException(int errCode, String errMsg, Throwable cause, Object... args) {
        super(errCode, errMsg, cause, args);
    }
}
