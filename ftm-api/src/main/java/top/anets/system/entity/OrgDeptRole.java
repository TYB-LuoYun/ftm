package top.anets.system.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import java.util.Date;
import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 
 * </p>
 *
 * @author ftm
 * @since 2022-09-07
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("sys_org_dept_role")
@ApiModel(value="OrgDeptRole对象", description="")
public class OrgDeptRole implements Serializable {

    private static final long serialVersionUID = 1L;

    private String id;

    @ApiModelProperty(value = "组织id")
    private String orgId;

    @ApiModelProperty(value = "部门id")
    private String depId;

    @ApiModelProperty(value = "角色id")
    private String roleId;

    @ApiModelProperty(value = "更新时间")
    private Date updateDate;


}
