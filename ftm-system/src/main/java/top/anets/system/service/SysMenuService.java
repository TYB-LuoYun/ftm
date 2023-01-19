package top.anets.system.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.springframework.web.bind.annotation.RequestMapping;
import top.anets.system.entity.SysMenu;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 菜单信息表 服务类
 * </p>
 *
 * @author ftm
 * @since 2021-07-26
 */
@RequestMapping("/sysMenuFeign")
public interface SysMenuService extends IService<SysMenu> {
    /**
     * 通过用户id查询所拥有的菜单权限信息
     * @param userId
     * @return
     */
    @RequestMapping("findByUserId")
    List<SysMenu> findByUserId(String userId);

    List<SysMenu> getRouterAllList();


    IPage pages(QueryWrapper querys, IPage page);
}
