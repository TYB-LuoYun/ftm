package top.anets.system.service;

import top.anets.entity.system.SysUser;
import com.baomidou.mybatisplus.extension.service.IService;
import top.anets.vo.system.SysUserCondition;

/**
 * <p>
 * 用户信息表 服务类
 * </p>
 *
 * @author ftm
 * @since 2021-07-26
 */

public interface SysUserService extends IService<SysUser> {
    /**
     * 通过用户名查询用户信息
     * @param username 用户名
     * @return
     */
    SysUser findByUsername(String username);

    void querys(SysUserCondition sysUserCondition);

    void saveOrUpdateHandle(SysUser user);

    void querysDetail(SysUserCondition condition, SysUser userInfo);
}
