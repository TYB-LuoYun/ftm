package top.anets.system.service;

import top.anets.entity.system.SysUser;
import top.anets.entity.system.UserCompany;
import com.baomidou.mybatisplus.extension.service.IService;
import top.anets.vo.system.UserCompanyCondition;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author ftm
 * @since 2022-02-14
 */
public interface UserCompanyService extends IService<UserCompany> {
    void querys(UserCompanyCondition condition);

    void getTreeDetail(UserCompanyCondition condition, SysUser userInfo);
}
