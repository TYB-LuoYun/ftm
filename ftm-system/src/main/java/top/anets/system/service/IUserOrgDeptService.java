package top.anets.system.service;

import top.anets.system.entity.UserOrgDept;
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
public interface IUserOrgDeptService extends IService<UserOrgDept> {


    IPage  pages(QueryWrapper querys, IPage page);

    /**
     * 添加
     *
     * @param userOrgDept 
     * @return int
     */
    int add(UserOrgDept userOrgDept);

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
     * @param userOrgDept 
     * @return int
     */
    int updateData(UserOrgDept userOrgDept);

    /**
     * id查询数据
     *
     * @param id id
     * @return UserOrgDept
     */
    UserOrgDept findById(Long id);


    /**
     * 关联查询
     */
    IPage pagesAssociate(Map<String, Object> params, IPage page);


}
