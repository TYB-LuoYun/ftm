package top.anets.system.service.impl;

import top.anets.system.entity.OrgDeptRole;
import top.anets.system.mapper.OrgDeptRoleMapper;
import top.anets.system.service.IOrgDeptRoleService;
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
public class OrgDeptRoleServiceImpl extends ServiceImpl<OrgDeptRoleMapper, OrgDeptRole> implements IOrgDeptRoleService {

    @Override
    public IPage pages(QueryWrapper  querys, IPage page) {
        return baseMapper.selectPage(page,querys );
    }

    @Override
    public int add(OrgDeptRole orgDeptRole){
        return baseMapper.insert(orgDeptRole);
    }

    @Override
    public int delete(Long id){
        return baseMapper.deleteById(id);
    }

    @Override
    public int updateData(OrgDeptRole orgDeptRole){
        return baseMapper.updateById(orgDeptRole);
    }

    @Override
    public OrgDeptRole findById(Long id){
        return  baseMapper.selectById(id);
    }

    @Override
    public IPage pagesAssociate(Map<String, Object>  params, IPage page) {
        return baseMapper.pagesAssociate(page ,params);
    }



}
