package top.anets.system.mapper;

import org.apache.ibatis.annotations.Param;
import top.anets.system.entity.SysMenu;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.List;

/**
 * <p>
 * 菜单信息表 Mapper 接口
 * </p>
 *
 * @author ftm
 * @since 2021-07-26
 */
public interface SysMenuMapper extends BaseMapper<SysMenu> {

    /**
     * 查询指定用户id所拥有的菜单权限（目录、菜单、按钮）
     * @param userId
     * @return
     */
    List<SysMenu> findByUserId(@Param("userId") String userId);
}
