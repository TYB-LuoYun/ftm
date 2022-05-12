package top.anets.system.mapper;

import top.anets.entity.system.SysUser;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import top.anets.vo.system.SysUserCondition;

import java.util.List;

/**
 * <p>
 * 用户信息表 Mapper 接口
 * </p>
 *
 * @author ftm
 * @since 2021-07-26
 */
public interface SysUserMapper extends BaseMapper<SysUser> {
    List<SysUser> querys(SysUserCondition condition);

    List<SysUser> getUsersOfCompany(String id);


    SysUser queryOne(String id);
}
