package top.anets.file.utils;

/**
 * @author ftm
 * @date 2023-12-21 16:39
 */

import cn.hutool.core.util.StrUtil;

public class BaseUncheckedException extends RuntimeException   {
    private static final long serialVersionUID = -778887391066124051L;
    private String message;
    private int code;

    public BaseUncheckedException(Throwable cause) {
        super(cause);
    }

    public BaseUncheckedException(final int code, Throwable cause) {
        super(cause);
        this.code = code;
    }

    public BaseUncheckedException(final int code, final String message) {
        super(message);
        this.code = code;
        this.message = message;
    }

    public BaseUncheckedException(final int code, final String message, Throwable cause) {
        super(cause);
        this.code = code;
        this.message = message;
    }

    public BaseUncheckedException(final int code, final String format, Object... args) {
        super(StrUtil.contains(format, "{}") ? StrUtil.format(format, args) : String.format(format, args));
        this.code = code;
        this.message = StrUtil.contains(format, "{}") ? StrUtil.format(format, args) : String.format(format, args);
    }

    public String getMessage() {
        return this.message;
    }

    public int getCode() {
        return this.code;
    }
}
