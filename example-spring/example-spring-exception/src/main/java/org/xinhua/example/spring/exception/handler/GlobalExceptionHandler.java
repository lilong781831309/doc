package org.xinhua.example.spring.exception.handler;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.xinhua.example.spring.exception.exception.AuthenticationException;
import org.xinhua.example.spring.exception.exception.BusinessException;
import org.xinhua.example.spring.exception.util.JacksonUtil;
import org.xinhua.example.spring.exception.util.ResultEntity;
import org.xinhua.example.spring.exception.util.ResultUtil;

@ControllerAdvice
public class GlobalExceptionHandler {
    private static Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    private ReloadableResourceBundleMessageSource messageSource;

    public GlobalExceptionHandler() {
        messageSource = new ReloadableResourceBundleMessageSource();
        messageSource.setBasename("i18n/messages");
    }

    @ResponseBody
    @ExceptionHandler({BindException.class})
    public ResponseEntity<ResultEntity> handleException(BindException e) {
        String errMsg = JacksonUtil.objectToJson(e.getAllErrors());
        ResultEntity result = ResultUtil.fail(errMsg);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(result);
    }

    @ResponseBody
    @ExceptionHandler({MissingServletRequestParameterException.class})
    public ResponseEntity<ResultEntity> handleException(MissingServletRequestParameterException e) {
        ResultEntity result = ResultUtil.fail(e.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(result);
    }

    @ResponseBody
    @ExceptionHandler({MethodArgumentNotValidException.class})
    public ResponseEntity<ResultEntity> handleException(MethodArgumentNotValidException e) {
        String errMsg = JacksonUtil.objectToJson(e.getBindingResult().getAllErrors());
        ResultEntity result = ResultUtil.fail(errMsg);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(result);
    }

    @ResponseBody
    @ExceptionHandler({AuthenticationException.class})
    public ResponseEntity<ResultEntity> handleException(AuthenticationException e) {
        String errMsg = getLocaleMessage(e.getErrCode(), e.getArgs(), e.getMessage());
        ResultEntity result = ResultUtil.fail(e.getErrCode(), errMsg, "发生异常");
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(result);
    }

    @ResponseBody
    @ExceptionHandler({BusinessException.class})
    public ResponseEntity<ResultEntity> handleException(BusinessException e) {
        String errMsg = getLocaleMessage(e.getErrCode(), e.getArgs(), e.getMessage());
        ResultEntity result = ResultUtil.fail(e.getErrCode(), errMsg, "发生异常");
        return ResponseEntity.status(HttpStatus.OK).body(result);
    }

    @ResponseBody
    @ExceptionHandler({Exception.class})
    public ResponseEntity<ResultEntity> handleException(Exception e) {
        String errMsg = getLocaleMessage(ResultUtil.ERR_CODE_FAIL, null, e.getMessage());
        ResultEntity result = ResultUtil.fail(ResultUtil.ERR_CODE_FAIL, errMsg, "发生异常");
        logger.error(result.getErrMsg(), e);
        return ResponseEntity.status(HttpStatus.OK).body(result);
    }

    private String getLocaleMessage(int code, Object[] params, String defaultMsg) {
        return messageSource.getMessage(code + "", params, defaultMsg, LocaleContextHolder.getLocale());
    }

}