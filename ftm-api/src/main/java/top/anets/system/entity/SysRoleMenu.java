package top.anets.system.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 角色权限表
 * </p>
 *
 * @author ftm
 * @since 2021-07-26
 */
@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="SysRoleMenu对象", description="角色权限表")
public class SysRoleMenu extends Model<SysRoleMenu> {

    private static final long serialVersionUID=1L;

    @ApiModelProperty(value = "主键 ID")
      @TableId(value = "id", type = IdType.AUTO)
    private String id;

    @ApiModelProperty(value = "角色 ID")
    private String roleId;

    @ApiModelProperty(value = "菜单 ID")
    private String menuId;



}
