/**
 *
 */
package top.anets.common.utils.exception;

import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import top.anets.common.utils.base.Result;
import top.anets.common.utils.module.data.RegexUtil;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.List;

/**
 * @author Administrator
 *
 */
@RestControllerAdvice
@Slf4j
@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
public class GlobalExceptionHandler {
//	未知异常
	@ExceptionHandler(value = Exception.class)
    public Result doException(Exception e) {
		StringWriter sw = new StringWriter();
        e.printStackTrace(new PrintWriter(sw, true));
        String trace = sw.toString();
        Throwable lastCause = getLastCause(e.getCause());
        if(lastCause !=null && lastCause instanceof ServiceException){
           return doServiceException((ServiceException) lastCause);
        }
        log.info(trace);
        log.error(trace);
    	return Result.error("未知异常:"+e.getMessage(), "||detail:"+trace);
    }

//	业务异常
    @ExceptionHandler(ServiceException.class)
    public Result doServiceException(ServiceException e) {
    	log.info(e.getMessage());
    	return Result.error(e.getCode(), e.getMessage(), null);
    }

    public Throwable  getLastCause(Throwable cause){
        if(cause == null){
            return null;
        }
	    if(cause.getCause()!=null){
	        return this.getLastCause(cause.getCause());
        }
        return cause;
    }


    /**
     * 处理所有RequestBody注解参数验证异常
     * @param e
     * @return
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Result handleMethodArgumentNotValidException(MethodArgumentNotValidException e){
    	 /*注意：此处的BindException 是 Spring 框架抛出的Validation异常*/
    	MethodArgumentNotValidException ex = (MethodArgumentNotValidException)e;

    	FieldError fieldError = ex.getBindingResult().getFieldError();
        if(fieldError!=null) log.warn("必填校验异常:{}({})", fieldError.getDefaultMessage(),fieldError.getField());

        String errorMsg = ex.getBindingResult().getAllErrors().get(0).getDefaultMessage();
        return Result.error("参数校验不通过:"+errorMsg);
    }


    @ResponseBody
    @ExceptionHandler({HttpMessageNotReadableException.class})
    public Result handleInvalidEnumValueException(HttpMessageNotReadableException ex) {
        String message = ex.getCause().getMessage();

        if (message.startsWith("Cannot deserialize value of type")) {
            try {
                InvalidFormatException a = (InvalidFormatException)ex.getCause();
                List<String> match = RegexUtil.findStrByLikeMatch("\\[\"", "\"\\]", message);
                // Throw custom exception with error message
                String errorMessage = match.get(0)+":无效值"+a.getValue();
                return Result.error(errorMessage);
            }catch (Exception e){
                throw new ServiceException(ex.getMessage());
            }
        }else{
            throw new ServiceException(ex.getMessage());
        }
    }


    /**
     * 处理所有RequestParam注解数据验证异常
     * @param ex
     * @return
     */
    @ExceptionHandler(BindException.class)
    public Result handleBindException(BindException ex) {
        FieldError fieldError = ex.getBindingResult().getFieldError();
        if(fieldError!=null) log.warn("必填校验异常:{}({})", fieldError.getDefaultMessage(),fieldError.getField());

        String defaultMessage = ex.getBindingResult().getAllErrors().get(0).getDefaultMessage();
        return Result.error("参数校验不通过:"+defaultMessage);
    }






}
