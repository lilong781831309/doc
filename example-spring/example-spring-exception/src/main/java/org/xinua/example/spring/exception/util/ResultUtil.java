package org.xinua.example.spring.exception.util;

public class ResultUtil {

    //HTTP请求结果
    public static final int ERR_CODE_SUCCESS = 0;
    public static final int ERR_CODE_FAIL = -1;

    public static final String ERR_MSG_SUCCESS = "";
    public static final String ERR_MSG_FAIL = "未知错误";

    public static final String MSG_SUCCESS = "请求成功";
    public static final String MSG_FAIL = "请求失败";

    public static ResultEntity success() {
        return success(MSG_SUCCESS);
    }

    public static ResultEntity success(String msg) {
        return success(null, msg);
    }

    public static ResultEntity success(Object content) {
        return success(content, MSG_SUCCESS);
    }

    public static ResultEntity success(Object content, String msg) {
        ResultEntity resultEntity = new ResultEntity();
        resultEntity.setErrCode(ERR_CODE_SUCCESS);
        resultEntity.setErrMsg(ERR_MSG_SUCCESS);
        resultEntity.setMsg(msg);
        resultEntity.setContent(content);
        return resultEntity;
    }

    public static ResultEntity fail() {
        return fail(ERR_MSG_FAIL);
    }

    public static ResultEntity fail(String errMsg) {
        return fail(ERR_CODE_FAIL, errMsg);
    }

    public static ResultEntity fail(int errCode, String errMsg) {
        return fail(errCode, errMsg, MSG_FAIL);
    }

    public static ResultEntity fail(int errCode, String errMsg, String msg) {
        ResultEntity resultEntity = new ResultEntity();
        resultEntity.setErrCode(errCode);
        resultEntity.setErrMsg(errMsg);
        resultEntity.setMsg(msg);
        return resultEntity;
    }
}
