package top.anets.system.service;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import top.anets.system.entity.SysUser;
import com.baomidou.mybatisplus.extension.service.IService;
import top.anets.system.vo.SysUserCondition;

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


    SysUser findByPhone(String mobile);
}
