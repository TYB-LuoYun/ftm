package top.anets.entity.system;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import java.util.Date;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 用户信息表
 * </p>
 *
 * @author ftm
 * @since 2021-07-26
 */
@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="SysUser对象", description="用户信息表")
public class SysUser extends Model<SysUser> {

    private static final long serialVersionUID=1L;

    @ApiModelProperty(value = "用户 ID")
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private String id;

    @ApiModelProperty(value = "用户名")
    private String username;

    @ApiModelProperty(value = "密码，加密存储, admin/1234")
    private String password;

    @ApiModelProperty(value = "帐户是否过期(1 未过期，0已过期)")
    public Boolean isAccountNonExpired;

    @ApiModelProperty(value = "帐户是否被锁定(1 未过期，0已过期)")
    public Boolean isAccountNonLocked;

    @ApiModelProperty(value = "密码是否过期(1 未过期，0已过期)")
    public Boolean isCredentialsNonExpired;

    @ApiModelProperty(value = "帐户是否可用(1 可用，0 删除用户)")
    public Boolean    isEnabled;

    @ApiModelProperty(value = "昵称")
    private String nickName;

    @ApiModelProperty(value = "头像url")
    private String imageUrl;

    @ApiModelProperty(value = "注册手机号")
    private String mobile;

    @ApiModelProperty(value = "注册邮箱")
    private String email;

    @ApiModelProperty(value = "创建时间")
    private Date createDate;

    @ApiModelProperty(value = "更新时间")
    private Date updateDate;

    @ApiModelProperty(value = "密码更新时间")
    private Date pwdUpdateDate;



    @TableField(exist = false)
    private Company company;

    @TableField(exist = false)
    private String companyId;

    @TableField(exist = false)
    private String deptId;




    @Override
    protected Serializable pkVal() {
        return this.id;
    }

}
