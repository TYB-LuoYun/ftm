package top.anets.system.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import java.util.Date;
import java.util.List;

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
 * @since 2022-08-12
 */
@Data
@Accessors(chain = true)
@TableName("dict")
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="Dict对象", description="")
public class Dict extends Model<Dict> {

    private static final long serialVersionUID=1L;
    @TableId("id")
    private String id;

    @ApiModelProperty(value = "字典名")
    @TableField("name")
    private String name;

    @ApiModelProperty(value = "描述")
    @TableField("description")
    private String description;

    @ApiModelProperty(value = "字典码")
    @TableField("code")
    private String code;

    @ApiModelProperty(value = "字典值")
    @TableField("value")
    private String value;

    @TableField("level")
    private Integer  level;

    @TableField("sort")
    private Integer sort;
    @TableField("insets_url")
    private String insetsUrl;
    @TableField("img_url")
    private String imgUrl;

    @TableField("link_url")
    private String linkUrl;
    @TableField("update_time")
    private Date updateTime;

    @TableField("parent_id")
    private String parentId;

    @TableField("is_leaf")
    private Boolean isLeaf;


    @TableField("is_dir")
    private Boolean isDir;

    @TableField("state")
    private Boolean state;
    @TableField("deleted")
    @TableLogic(value = "0",delval="1")
    private Integer deleted;

    private String attr1;

    private String attr2;

    private String attr3;

    private String attr4;

    private String attr5;




    @TableField(exist = false)
    private List<Dict> children;

    @TableField(exist = false)
    private String label;

}
