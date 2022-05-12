package top.anets.system.controller;


import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;
import top.anets.entity.system.Company;
import top.anets.entity.system.SysUser;
import top.anets.system.config.AuthUtil;
import top.anets.system.service.CompanyService;
import top.anets.utils.base.Result;
import top.anets.vo.system.CompanyVo;

import java.util.List;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author ftm
 * @since 2022-02-14
 */
@RestController
@RequestMapping("/company")
public class CompanyController {
    @Autowired
    private CompanyService companyService;

    @RequestMapping("/getCompanysSelfAndChildren")
    public Result getSelfAndChildren(){
        SysUser userInfo = AuthUtil.getUserInfo();
        List<Company> items =  companyService.getSelfAndChildren(userInfo.getCompany().getId());
       return Result.success(items);
    }


    @RequestMapping("getCompanys")
    public Result getCompanys(@RequestBody CompanyVo companyVo){
        if(StringUtils.isBlank(companyVo.getParentId())){
            companyVo.setParentId(AuthUtil.getUserInfo().getCompany().getId());
        }
        companyService.querys(companyVo);
        return Result.success(companyVo);
    }
}

