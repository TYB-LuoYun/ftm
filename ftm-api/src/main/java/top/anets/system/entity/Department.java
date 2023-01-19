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
 * 
 * </p>
 *
 * @author ftm
 * @since 2022-02-14
 */
@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="Department对象", description="")
public class Department extends Model<Department> {

    private static final long serialVersionUID=1L;

      @TableId(value = "id", type = IdType.ASSIGN_ID)
    private String id;

    private String companyId;

    @ApiModelProperty(value = "部门名称")
    private String name;

    @ApiModelProperty(value = "部门编号")
    private String code;

    @ApiModelProperty(value = "备注")
    private String remark;

    private Date createdDate;

    private Date updateDate;




}
