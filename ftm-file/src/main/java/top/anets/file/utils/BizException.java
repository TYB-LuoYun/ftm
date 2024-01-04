package top.anets.file.utils;

/**
 * @author ftm
 * @date 2023-12-21 17:05
 */
public class BizException extends BaseUncheckedException {
    private static final long serialVersionUID = -3843907364558373817L;

    public BizException(Throwable cause) {
        super(cause);
    }

    public BizException(int code, Throwable cause) {
        super(code, cause);
    }

    public BizException(String message) {
        super(-1, message);
    }

    public BizException(String message, Throwable cause) {
        super(-1, message, cause);
    }

    public BizException(int code, String message) {
        super(code, message);
    }

    public BizException(int code, String message, Throwable cause) {
        super(code, message, cause);
    }

    public BizException(int code, String message, Object... args) {
        super(code, message, args);
    }

    public static BizException wrap(int code, String message, Object... args) {
        return new BizException(code, message, args);
    }

    public static BizException wrap(String message, Object... args) {
        return new BizException(-1, message, args);
    }

    public static BizException validFail(String message, Object... args) {
        return new BizException(-9, message, args);
    }

    public static BizException wrap(BaseExceptionCode ex) {
        return new BizException(ex.getCode(), ex.getMsg());
    }

    public static BizException wrap(BaseExceptionCode ex, Throwable cause) {
        return new BizException(ex.getCode(), ex.getMsg(), cause);
    }

    public String toString() {
        return "BizException [message=" + this.getMessage() + ", code=" + this.getCode() + "]";
    }
}
