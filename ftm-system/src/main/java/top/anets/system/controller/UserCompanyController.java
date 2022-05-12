package top.anets.system.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;
import top.anets.entity.system.SysUser;
import top.anets.system.config.AuthUtil;
import top.anets.system.service.SysUserService;
import top.anets.system.service.UserCompanyService;
import top.anets.utils.base.Result;
import top.anets.vo.system.SysUserCondition;
import top.anets.vo.system.UserCompanyCondition;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author ftm
 * @since 2022-02-14
 */
@RestController
@RequestMapping("/user-company")
public class UserCompanyController {
    @Autowired
    private UserCompanyService userCompanyService;

    @RequestMapping("/queryUserCompanys")
    public Result querys(@RequestBody UserCompanyCondition condition){

        //获取当前用户所在的公司
        SysUser userInfo = AuthUtil.getUserInfo();
        userCompanyService.getTreeDetail(condition,userInfo);
        return Result.success(condition);
    }
}

