package top.anets.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;
import top.anets.entity.system.Meta;
import top.anets.entity.system.SysMenu;
import top.anets.system.mapper.SysMenuMapper;
import top.anets.system.service.SysMenuService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import java.util.List;

/**
 * <p>
 * 菜单信息表 服务实现类
 * </p>
 *
 * @author ftm
 * @since 2021-07-26
 */
@RestController
public class SysMenuServiceImpl extends ServiceImpl<SysMenuMapper, SysMenu> implements SysMenuService {



    @Autowired
    private SysMenuMapper sysMenuMapper;
    /**
     * 通过用户id查询所拥有的菜单权限信息
     *
     * @param userId
     * @return
     */
    @Override
    public List<SysMenu> findByUserId(String userId) {
        // 通过用户id查询权限信息
        List<SysMenu> menuList = sysMenuMapper.findByUserId(userId);
        if( CollectionUtils.isEmpty(menuList)
                || menuList.get(0) == null ) {
            return null;
        }
        return menuList;
    }

    @Override
    public List<SysMenu> getRouterAllList() {
        return this.getRouterAllList(null);
    }

    private List<SysMenu> getRouterAllList(String id) {
//      根据ID去查询子节点
        QueryWrapper<SysMenu> sysMenuQueryWrapper = new QueryWrapper<>();
        if(id==null){
            sysMenuQueryWrapper.isNull("parent_id");
        }else{
            sysMenuQueryWrapper.eq("parent_id", id);
        }
        List<SysMenu> sysMenus = sysMenuMapper.selectList(sysMenuQueryWrapper);
        if(CollectionUtils.isEmpty(sysMenus)){
            return null;
        }
        for (SysMenu item:sysMenus) {
            item.setChildren(this.getRouterAllList(item.getId()));
            Meta meta = new Meta();
            meta.setTitle(item.getMetaTitle());
            meta.setIcon(item.getMetaIcon());
            meta.setBreadcrumbHidden(item.getMetaBreadcrumbHidden());
            meta.setNoClosable(item.getMetaNoClosable());
            item.setMeta(meta);
        }

        return sysMenus;
    }
}
