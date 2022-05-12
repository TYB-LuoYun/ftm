package top.anets.entity.system;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 用户角色关系表
 * </p>
 *
 * @author ftm
 * @since 2021-07-26
 */
@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="SysUserRole对象", description="用户角色关系表")
public class SysUserRole extends Model<SysUserRole> {

    private static final long serialVersionUID=1L;

    @ApiModelProperty(value = "主键 ID")
      @TableId(value = "id", type = IdType.AUTO)
    private String id;

    @ApiModelProperty(value = "用户 ID")
    private String userId;

    @ApiModelProperty(value = "角色 ID")
    private String roleId;


    @Override
    protected Serializable pkVal() {
        return this.id;
    }

}
