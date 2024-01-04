package top.anets.cloud.base.feign.exception;

/**
 * @author ftm
 * @date 2023-11-01 13:48
 */
public class FeignException  extends RuntimeException{
    private String code ;
    private String message ;

    public FeignException() {
        super();
    }

    public FeignException(String message,Throwable cause) {
        super(message, cause);
        this.message=message;
    }

    public FeignException(String message) {
        super(message);
        this.message=message;
    }


    public FeignException(String code ,String message) {
        super(message);
        this.code=code;
        this.message=message;
    }

    public FeignException(Throwable cause) {
        super(cause);
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

}