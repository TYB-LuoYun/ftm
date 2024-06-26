package top.anets.boot.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ResultEnum {

    SUCCESS("200", "成功"),

    ERROR("999", "错误"),


    UN_AUTHENTICATED("401", "身份认证失败(no authentication)"),

    UN_AUTHORIZATION("401", "授权失败，没有权限访问(no authorization)"),

    AUTH_FAIL("1400", "认证失败"),

    // token异常
    TOKEN_PAST("1401", "身份过期，请求重新登录！"), TOKEN_ERROR("1402", "令牌错误"),

    HEADEA_ERROR("1403", "请求头错误"),

    AUTH_USERNAME_NONE("1405", "用户名不能为空"), AUTH_PASSWORD_NONE("1406", "密码不能为空"),

    MENU_NO("306", "没此权限，请联系管理员！");

    private String code;
    private String desc;
}
