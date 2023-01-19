package top.anets.system.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import java.util.Date;
import java.util.List;

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
@ApiModel(value="Company对象", description="")
public class Company extends Model<Company> {

    private static final long serialVersionUID=1L;

    private String id;

    @ApiModelProperty(value = "公司名称")
    private String name;

    @ApiModelProperty(value = "公司地址")
    private String address;

    @ApiModelProperty(value = "邮箱")
    private String email;

    @ApiModelProperty(value = "公司简介")
    private String introduction;

    @ApiModelProperty(value = "logo")
    private String logoImage;
    private String parentId;
    private String level;
    private String label;
    private String origin;

    private Date createDate;

    private Date updateData;



    @TableField(exist = false)
    private List<Company> children;

    @TableField(exist = false)
    private List<Department> depts;

}
