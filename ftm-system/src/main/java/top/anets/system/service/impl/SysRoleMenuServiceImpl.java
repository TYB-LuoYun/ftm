package top.anets.system.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import top.anets.system.entity.SysMenu;
import top.anets.system.entity.SysRole;
import top.anets.system.entity.SysRoleMenu;
import top.anets.system.model.ResourceRoles;
import top.anets.system.mapper.SysRoleMenuMapper;
import top.anets.system.service.SysMenuService;
import top.anets.system.service.SysRoleMenuService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import top.anets.system.service.SysRoleService;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * <p>
 * 角色权限表 服务实现类
 * </p>
 *
 * @author ftm
 * @since 2021-07-26
 */
@Service
public class SysRoleMenuServiceImpl extends ServiceImpl<SysRoleMenuMapper, SysRoleMenu> implements SysRoleMenuService {
    @Autowired
    private SysMenuService sysMenuService;
    @Autowired
    private SysRoleMenuService sysRoleMenuService;
    @Autowired
    private SysRoleService sysRoleService;
    @Override
    public List<ResourceRoles> loadResourceRoles() {
        List<SysMenu> list = sysMenuService.list();
        if(CollectionUtils.isEmpty(list)){
            return null;
        }
        List<ResourceRoles> resourceRolesList = new LinkedList<>();
        list.forEach(item->{
            String url = item.getUrl();
            if(StringUtils.isNotBlank(url)){
                ResourceRoles resourceRoles = new ResourceRoles();
                resourceRoles.setResource(url);
//               查询该资源对应的角色
                List<SysRoleMenu> list1 = sysRoleMenuService.list(Wrappers.<SysRoleMenu>lambdaQuery().eq(SysRoleMenu::getMenuId, item.getId()));
                if(CollectionUtils.isEmpty(list1)){
                    return;
                }
                ArrayList<String> roles = new ArrayList<>();
                list1.forEach(each->{
                    SysRole byId = sysRoleService.getById(each.getRoleId());
                    if(byId!=null){
                        roles.add(byId.getCode());
                    }
                });
                resourceRoles.setRoles(roles);
                resourceRolesList.add(resourceRoles);
            }
        });
        return resourceRolesList;
    }
}
