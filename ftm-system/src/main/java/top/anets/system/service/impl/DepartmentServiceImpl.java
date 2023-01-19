package top.anets.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import top.anets.system.entity.Department;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import top.anets.system.mapper.DepartmentMapper;
import top.anets.system.service.DepartmentService;

import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author ftm
 * @since 2022-02-14
 */
@Service
public class DepartmentServiceImpl extends ServiceImpl<DepartmentMapper, Department> implements DepartmentService {

    @Autowired
    private DepartmentMapper departmentMapper;

    @Override
    public List<Department> querys(String companyId) {
        QueryWrapper<Department> wrapper = new QueryWrapper<>();
        wrapper.eq("company_id", companyId);
        List<Department> departments = departmentMapper.selectList(wrapper);
        return departments;
    }
}
