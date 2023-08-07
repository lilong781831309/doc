package org.xinhua.example.spring.mybatisplus.handler;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.xinhua.example.spring.mybatisplus.exception.WechatException;
import org.xinhua.example.spring.mybatisplus.model.vo.ResultEntity;
import org.xinhua.example.spring.mybatisplus.util.JacksonUtil;
import org.xinhua.example.spring.mybatisplus.util.ResultUtil;

@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler {

    public GlobalExceptionHandler() {
    }

    @ExceptionHandler(WechatException.class)
    @ResponseBody
    public ResponseEntity<ResultEntity> handleWechatException(WechatException e) {
        ResultEntity result = this.getResultEntity(e.getErrcode(), e.getErrmsg());
        log.error(result.getErrMsg(), e);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(result);
    }

    @ExceptionHandler({BindException.class})
    @ResponseBody
    public ResponseEntity<ResultEntity> validationBindExceptionHandler(BindException e) {
        ResultEntity result = this.getResultEntity(JacksonUtil.objectToJson(e.getAllErrors()));
        log.error(result.getErrMsg(), e);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(result);
    }

    @ExceptionHandler({MethodArgumentNotValidException.class})
    @ResponseBody
    public ResponseEntity<ResultEntity> validationBindExceptionHandler(MethodArgumentNotValidException e) {
        ResultEntity result = this.getResultEntity(JacksonUtil.objectToJson(e.getBindingResult().getAllErrors()));
        log.error(result.getErrMsg(), e);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(result);
    }

    @ExceptionHandler({Exception.class})
    @ResponseBody
    public ResponseEntity<ResultEntity> exceptionHandler(Exception e) {
        ResultEntity result = this.getResultEntity(e.getMessage());
        log.error(result.getErrMsg(), e);
        return ResponseEntity.status(HttpStatus.OK).body(result);
    }

    private ResultEntity getResultEntity(String errMsg) {
        return ResultUtil.fail(errMsg);
    }

    private ResultEntity getResultEntity(int errCode, String errMsg) {
        return ResultUtil.fail(errCode, errMsg);
    }
}