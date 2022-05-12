package top.anets.oauth2.service;

import com.alibaba.fastjson.annotation.JSONField;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import top.anets.entity.system.SysUser;

import java.util.List;

@Data
public class JwtUser extends SysUser implements UserDetails {

//    @ApiModelProperty(value = "用户ID")
//    private String uid;
//
//    @ApiModelProperty(value = "用户名")
//    private String username;
//
    @JSONField(serialize = false) // 忽略转json
    @ApiModelProperty(value = "密码，加密存储, admin/1234")
    private String password;
//
//    @ApiModelProperty(value = "昵称")
//    private String nickName;
//
//    @ApiModelProperty(value = "头像url")
//    private String imageUrl;
//
//    @ApiModelProperty(value = "注册手机号")
//    private String mobile;
//
//    @ApiModelProperty(value = "注册邮箱")
//    private String email;
//
//    @JSONField(serialize = false) // 忽略转json
//    @ApiModelProperty(value = "帐户是否过期(1 未过期，0已过期)")
//    private boolean isAccountNonExpired; // 不要写小写 boolean
//
//    @JSONField(serialize = false) // 忽略转json
//    @ApiModelProperty(value = "帐户是否被锁定(1 未过期，0已过期)")
//    private boolean isAccountNonLocked;
//
//    @JSONField(serialize = false) // 忽略转json
//    @ApiModelProperty(value = "密码是否过期(1 未过期，0已过期)")
//    private boolean isCredentialsNonExpired;
//
//    @JSONField(serialize = false) // 忽略转json
//    @ApiModelProperty(value = "帐户是否可用(1 可用，0 删除用户)")
//    private  boolean isEnabled;

    /**
     * 封装用户拥有的菜单权限标识
     */
    @JSONField(serialize = false) // 忽略转json
    private List<GrantedAuthority> authorities;

    public JwtUser(){
        super();
    }

//    public JwtUser(String uid, String username, String password,
//                   String nickName, String imageUrl, String mobile, String email,
//                   Boolean isAccountNonExpired, Boolean isAccountNonLocked,
//                   Boolean isCredentialsNonExpired, Boolean isEnabled,
//                   List<GrantedAuthority> authorities) {
//        uid = uid;
//        username = username;
//        password = password;
//        nickName = nickName;
//        imageUrl = imageUrl;
//        mobile = mobile;
//        email = email;
//        isAccountNonExpired = isAccountNonExpired  ;
//        isAccountNonLocked = isAccountNonLocked  ;
//        isCredentialsNonExpired = isCredentialsNonExpired  ;
//        isEnabled = isEnabled;
//        this.authorities = authorities;
//    }

    /**
     * Indicates whether the user's account has expired. An expired account cannot be
     * authenticated.
     *
     * @return <code>true</code> if the user's account is valid (ie non-expired),
     * <code>false</code> if no longer valid (ie expired)
     */
    @Override
    public boolean isAccountNonExpired() {
        return isAccountNonExpired;
    }

    /**
     * Indicates whether the user is locked or unlocked. A locked user cannot be
     * authenticated.
     *
     * @return <code>true</code> if the user is not locked, <code>false</code> otherwise
     */
    @Override
    public boolean isAccountNonLocked() {
        return isAccountNonLocked;
    }

    /**
     * Indicates whether the user's credentials (password) has expired. Expired
     * credentials prevent authentication.
     *
     * @return <code>true</code> if the user's credentials are valid (ie non-expired),
     * <code>false</code> if no longer valid (ie expired)
     */
    @Override
    public boolean isCredentialsNonExpired() {
        return isCredentialsNonExpired;
    }

    /**
     * Indicates whether the user is enabled or disabled. A disabled user cannot be
     * authenticated.
     *
     * @return <code>true</code> if the user is enabled, <code>false</code> otherwise
     */
    @Override
    public boolean isEnabled() {
        return isEnabled;
    }
}