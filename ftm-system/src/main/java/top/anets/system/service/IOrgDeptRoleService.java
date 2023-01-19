package top.anets.system.service;

import top.anets.system.entity.OrgDeptRole;
import com.baomidou.mybatisplus.extension.service.IService;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import java.util.Map;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author ftm
 * @since 2022-09-07
 */
public interface IOrgDeptRoleService extends IService<OrgDeptRole> {


    IPage  pages(QueryWrapper querys, IPage page);

    /**
     * 添加
     *
     * @param orgDeptRole 
     * @return int
     */
    int add(OrgDeptRole orgDeptRole);

    /**
     * 删除
     *
     * @param id 主键
     * @return int
     */
    int delete(Long id);

    /**
     * 修改
     *
     * @param orgDeptRole 
     * @return int
     */
    int updateData(OrgDeptRole orgDeptRole);

    /**
     * id查询数据
     *
     * @param id id
     * @return OrgDeptRole
     */
    OrgDeptRole findById(Long id);


    /**
     * 关联查询
     */
    IPage pagesAssociate(Map<String, Object> params, IPage page);


}
