package top.anets.system.vo;

import lombok.Data;
import top.anets.system.entity.Company;

import java.util.List;

@Data
public class CompanyVo extends Company {
    private long rowCount;
    private Integer pageSize  ;
    private Integer pageNum  ;
    private List<Company> items;
}
