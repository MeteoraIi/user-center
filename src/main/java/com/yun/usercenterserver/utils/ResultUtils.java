package com.yun.usercenterserver.utils;

import com.yun.usercenterserver.common.BaseResponse;
import com.yun.usercenterserver.common.ErrorCode;

/**
 * 返回工具类，帮助生成返回给前端的数据对象
 */
public class ResultUtils {

    // 方法中需要使用泛型变量时候，需要泛型参数
    public static <T> BaseResponse<T> sucess(T data) {
        return new BaseResponse<T>(0, data, "ok");
    }

    // 失败
    public static  BaseResponse error(ErrorCode errorCode) {
        return  new BaseResponse(errorCode);
    }

    public static  BaseResponse error(ErrorCode errorCode, String message, String description) {
        return  new BaseResponse(errorCode.getCode(), message, description);
    }

    public static  BaseResponse error(int code, String message, String description) {
        return  new BaseResponse<>(code, message, description);
    }

    public static  BaseResponse error(ErrorCode errorCode, String description) {
        return  new BaseResponse(errorCode.getCode(), errorCode.getMessage(), description);
    }
}
