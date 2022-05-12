package top.anets.vo.system;

import lombok.Data;
import top.anets.entity.system.Company;
import top.anets.entity.system.SysUser;

import java.util.List;

@Data
public class CompanyVo extends Company{
    private long rowCount;
    private Integer pageSize  ;
    private Integer pageNum  ;
    private List<Company> items;
}
