package org.xinhua.example.spring.rest.template.handler;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.xinhua.example.spring.rest.template.exception.WechatException;
import org.xinhua.example.spring.rest.template.util.ResultEntity;
import org.xinhua.example.spring.rest.template.util.ResultUtil;

@ControllerAdvice
public class GlobalExceptionHandler {
    private static Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    public GlobalExceptionHandler() {
    }

    @ResponseBody
    @ExceptionHandler({WechatException.class})
    public ResponseEntity<ResultEntity> handleException(WechatException e) {
        ResultEntity result = ResultUtil.fail(e.getErrcode(), e.getErrmsg(), "发生异常");
        return ResponseEntity.status(HttpStatus.OK).body(result);
    }

    @ResponseBody
    @ExceptionHandler({Exception.class})
    public ResponseEntity<ResultEntity> handleException(Exception e) {
        ResultEntity result = ResultUtil.fail(ResultUtil.ERR_CODE_FAIL, e.getMessage(), "发生异常");
        logger.error(result.getErrMsg(), e);
        return ResponseEntity.status(HttpStatus.OK).body(result);
    }

}