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
@TableName("sys_user_org_dept")
@ApiModel(value="UserOrgDept对象", description="")
public class UserOrgDept implements Serializable {

    private static final long serialVersionUID = 1L;

    private String id;

    private String uid;

    private String orgId;

    private String deptId;

    private Integer isEnable;

    private Date createDate;

    private Date updateDate;


}
