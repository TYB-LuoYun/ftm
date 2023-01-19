package top.anets.system.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import java.util.Date;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 角色信息表
 * </p>
 *
 * @author ftm
 * @since 2022-02-14
 */
@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="SysRole对象", description="角色信息表")
public class SysRole extends Model<SysRole> {

    private static final long serialVersionUID=1L;

    @ApiModelProperty(value = "角色 ID")
      @TableId(value = "id", type = IdType.ASSIGN_ID)
    private String id;

    private String deptId;

    @ApiModelProperty(value = "角色名称")
    private String name;

    @ApiModelProperty(value = "角色编码")
    private String code;

    @ApiModelProperty(value = "角色说明")
    private String remark;

    private Date createDate;

    private Date updateDate;


}
