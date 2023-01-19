package top.anets.system.service;

import top.anets.system.entity.SysRoleMenu;
import com.baomidou.mybatisplus.extension.service.IService;
import top.anets.system.model.ResourceRoles;

import java.util.List;

/**
 * <p>
 * 角色权限表 服务类
 * </p>
 *
 * @author ftm
 * @since 2021-07-26
 */
public interface SysRoleMenuService extends IService<SysRoleMenu> {

    List<ResourceRoles> loadResourceRoles();
}
