package org.xinhua.example.spring.mybatisplus.util;


import org.xinhua.example.spring.mybatisplus.model.vo.ResultEntity;

public class ResultUtil {

    //HTTP请求结果
    public static final int ERR_CODE_SUCCESS = 0;
    public static final int ERR_CODE_FAIL = -1;

    public static final String ERR_MSG_SUCCESS = "";
    public static final String ERR_MSG_FAIL = "未知错误";

    public static final String MSG_SUCCESS = "请求成功";
    public static final String MSG_FAIL = "请求失败";

    public static <T> ResultEntity<T> status(boolean success) {
        return success ? success() : fail();
    }

    public static <T> ResultEntity<T> success() {
        return success(MSG_SUCCESS);
    }

    public static <T> ResultEntity<T> success(String msg) {
        return success(null, msg);
    }

    public static <T> ResultEntity success(T content) {
        return success(content, MSG_SUCCESS);
    }

    public static <T> ResultEntity<T> success(T content, String msg) {
        ResultEntity resultEntity = new ResultEntity();
        resultEntity.setErrCode(ERR_CODE_SUCCESS);
        resultEntity.setErrMsg(ERR_MSG_SUCCESS);
        resultEntity.setMsg(msg);
        resultEntity.setContent(content);
        return resultEntity;
    }

    public static <T> ResultEntity<T> fail() {
        return fail(ERR_MSG_FAIL);
    }

    public static <T> ResultEntity<T> fail(String errMsg) {
        return fail(ERR_CODE_FAIL, errMsg);
    }

    public static <T> ResultEntity<T> fail(int errCode, String errMsg) {
        return fail(errCode, errMsg, MSG_FAIL);
    }

    public static <T> ResultEntity<T> fail(int errCode, String errMsg, String msg) {
        return ResultEntity.of(errCode, errMsg, msg, null);
    }
}
