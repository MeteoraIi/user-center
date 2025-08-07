package com.yun.usercenterserver.exception;

import com.yun.usercenterserver.common.BaseResponse;
import com.yun.usercenterserver.common.ErrorCode;
import com.yun.usercenterserver.utils.ResultUtils;
import lombok.extern.slf4j.Slf4j;

import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

// 全局异常处理器 Rest 风格
// 在内部各种处理器来处理各种异常，异常处理遵循 “精确匹配优先” 原则
// GlobalExceptionHandler 类是 Spring AOP 面向切面编程的典型应用，它通过 “切面” 思想将全局异常处理逻辑从业务代码中抽离出来，集中管理
// AOP 的核心是将分散在各个业务逻辑中的通用功能（如日志、事务、异常处理）抽离成一个独立的 “切面”，
// 在不修改业务代码的前提下，通过 “织入” 方式作用于多个目标方法。
@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    /**
     * 捕获 BusinessException 类型的异常
     * @param e
     * @return
     */
    @ExceptionHandler(BusinessException.class)
    public BaseResponse businessException(BusinessException e) {
        log.error("businessException: " + e.getMessage(), e);
        // 返回给前端的异常信息，也用 ResultUtils 生成
        // 这里根本没有 ErrorCode
        return ResultUtils.error(e.getCode(), e.getMessage(), e.getDescription());
    }

    /**
     * 捕获 RuntimeException 类型的异常
     * @param e
     * @return
     */
    @ExceptionHandler(RuntimeException.class)
    public BaseResponse runtimeException(RuntimeException e) {
        log.error("runtimeException: " + e.getMessage(), e);
        return ResultUtils.error(ErrorCode.SYSTEM_ERROR, e.getMessage(), "");
    }
}
