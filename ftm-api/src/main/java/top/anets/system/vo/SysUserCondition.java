package top.anets.system.vo;

import lombok.Data;
import top.anets.system.entity.SysUser;

import java.util.List;

@Data
public class SysUserCondition extends SysUser {
    private long rowCount;
    private Integer pageSize  ;
    private Integer pageNum  ;


    private String companyId;


    private List<SysUser> items;
}
