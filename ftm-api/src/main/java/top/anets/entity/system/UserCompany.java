package top.anets.entity.system;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import java.util.Date;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;
import java.util.List;

import io.swagger.annotations.ApiModel;
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
@ApiModel(value="UserCompany对象", description="")
public class UserCompany extends Model<UserCompany> {

    private static final long serialVersionUID=1L;

      @TableId(value = "id", type = IdType.ASSIGN_ID)
    private String id;

    private String uid;

    private String companyId;

    private Boolean isEnable;

    private Date createDate;

    private Date updateDate;



    @TableField(exist = false)
    private SysUser sysUser;
    @TableField(exist = false)
    private Company company;

    @TableField(exist = false)
    private List<Department> depts;

    @TableField(exist = false)
    private List<Company> companys;

    @TableField(exist = false)
    private String companyIdsStr;

    @Override
    protected Serializable pkVal() {
        return this.id;
    }

}
