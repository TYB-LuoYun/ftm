package top.anets.vo.system;

import lombok.Data;
import top.anets.entity.system.UserCompany;

import java.util.List;


@Data
public class UserCompanyCondition extends UserCompany {
    private long rowCount;
    private Integer pageSize  ;
    private Integer pageNum  ;

    private List<UserCompany>  userCompanys;



}
