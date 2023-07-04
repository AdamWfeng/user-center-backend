package fun.wanlu.usercenterbackend.exception;

import fun.wanlu.usercenterbackend.common.BaseResponse;
import fun.wanlu.usercenterbackend.common.ErrorCode;
import fun.wanlu.usercenterbackend.utils.ResultUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    /**
     * 全局 BusinessException 捕获
     *
     * @param e
     * @return
     */
    @ExceptionHandler(BusinessException.class)
    public BaseResponse<Object> businessException(BusinessException e) {
        log.error("BusinessException" + e.getMessage(), e);
        return ResultUtils.error(e.getCode(), e.getMessage(), e.getDescription());
    }

    /**
     * 全局 RuntimeException 捕获
     *
     * @param e
     * @return
     */
    @ExceptionHandler(RuntimeException.class)
    public BaseResponse<ErrorCode> runtimeException(RuntimeException e) {
        log.error("runtimeException", e);
        return ResultUtils.error(ErrorCode.SYSTEM_ERROR);
    }

    /**
     * 全局 Exception 捕获
     *
     * @param e
     * @return
     */
    @ExceptionHandler(Exception.class)
    public BaseResponse<ErrorCode> exception(Exception e) {
        log.error("exception", e);
        return ResultUtils.error(ErrorCode.SYSTEM_ERROR);
    }
}
