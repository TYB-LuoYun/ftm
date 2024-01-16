package top.anets.file.utils;

/**
 * @author ftm
 * @date 2023-12-21 16:37
 */
public class ArgumentException extends BaseUncheckedException {
    private static final long serialVersionUID = -3843907364558373817L;

    public ArgumentException(Throwable cause) {
        super(cause);
    }

    public ArgumentException(String message) {
        super(ExceptionCode.BASE_VALID_PARAM.getCode(), message);
    }

    public ArgumentException(String message, Throwable cause) {
        super(ExceptionCode.BASE_VALID_PARAM.getCode(), message, cause);
    }

    public ArgumentException(final String format, Object... args) {
        super(ExceptionCode.BASE_VALID_PARAM.getCode(), format, args);
    }

    public String toString() {
        return "ArgumentException [message=" + this.getMessage() + ", code=" + this.getCode() + "]";
    }
}
