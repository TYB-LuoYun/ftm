package top.anets.oauth2.exception;

import org.springframework.security.oauth2.common.exceptions.InvalidGrantException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import top.anets.common.utils.base.Result;

/**
 * @author ftm
 * @date 2023-08-22 16:17
 */
@RestControllerAdvice
public class AuthExceptionHandler {
    /*** 用户名和密码异常** @param e* @return*/
    @ExceptionHandler(InvalidGrantException.class)
    public Result handleInvalidGrantException(InvalidGrantException e) {return Result.error(e.getMessage()); }
}
