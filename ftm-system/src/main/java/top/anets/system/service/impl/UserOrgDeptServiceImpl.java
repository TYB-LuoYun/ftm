package top.anets.system.service.impl;

import top.anets.system.entity.UserOrgDept;
import top.anets.system.mapper.UserOrgDeptMapper;
import top.anets.system.service.IUserOrgDeptService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import java.util.Map;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author ftm
 * @since 2022-09-07
 */
@Service
public class UserOrgDeptServiceImpl extends ServiceImpl<UserOrgDeptMapper, UserOrgDept> implements IUserOrgDeptService {

    @Override
    public IPage pages(QueryWrapper  querys, IPage page) {
        return baseMapper.selectPage(page,querys );
    }

    @Override
    public int add(UserOrgDept userOrgDept){
        return baseMapper.insert(userOrgDept);
    }

    @Override
    public int delete(Long id){
        return baseMapper.deleteById(id);
    }

    @Override
    public int updateData(UserOrgDept userOrgDept){
        return baseMapper.updateById(userOrgDept);
    }

    @Override
    public UserOrgDept findById(Long id){
        return  baseMapper.selectById(id);
    }

    @Override
    public IPage pagesAssociate(Map<String, Object>  params, IPage page) {
        return baseMapper.pagesAssociate(page ,params);
    }



}
