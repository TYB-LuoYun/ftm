package top.anets.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import top.anets.entity.system.*;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import top.anets.system.mapper.CompanyMapper;
import top.anets.system.mapper.DepartmentMapper;
import top.anets.system.mapper.UserCompanyMapper;
import top.anets.system.mapper.UserDeptMapper;
import top.anets.system.service.UserCompanyService;
import top.anets.vo.system.UserCompanyCondition;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author ftm
 * @since 2022-02-14
 */
@Slf4j
@Service
public class UserCompanyServiceImpl extends ServiceImpl<UserCompanyMapper, UserCompany> implements UserCompanyService {
    @Autowired
    private UserCompanyMapper userCompanyMapper;


    @Autowired
    private CompanyMapper companyMapper;


    @Autowired
    private DepartmentMapper departmentMapper;



    @Autowired
    private UserDeptMapper userDeptMapper;


    @Override
    public void querys(UserCompanyCondition condition) {
        if(condition.getPageSize()==null){
            List<UserCompany> list = userCompanyMapper.querys(condition);
            condition.setUserCompanys(list);
            return;
        }
        PageHelper.startPage(condition.getPageNum(),condition.getPageSize());
        List<UserCompany> list = userCompanyMapper.querys(condition);
        PageInfo<UserCompany> pageInfo = new PageInfo<>(list);
        condition.setUserCompanys(list);
        long total = pageInfo.getTotal();
        condition.setRowCount(total);
    }

    @Override
    public void getTreeDetail(UserCompanyCondition condition, SysUser userInfo) {
//      根据公司id查到子集
        List<String> ids = new ArrayList<>();
        ids.add(userInfo.getCompany().getId());
        List<Company>  a =this.getChildren(userInfo.getCompany().getId(),ids);


//      ids就是本公司和所有下级的公司
        PageHelper.startPage(condition.getPageNum(),condition.getPageSize());
        List<UserCompany> list = userCompanyMapper.getUserAndCompany(ids);
        PageInfo<UserCompany> pageInfo = new PageInfo<>(list);



        for (UserCompany item: list ) {
//          找公司
            String[] split = item.getCompanyIdsStr().split(",");
            if(split!=null&&split.length>=0){
                List<Company> list1 = companyMapper.queryListIn(split);
                for (Company eve: list1 ) {
                    //          找部门id，根据用户id找到部门id，
                    QueryWrapper<UserDept> userDeptQueryWrapper = new QueryWrapper<>();
                    userDeptQueryWrapper.eq("uid", item.getSysUser().getId());
                    userDeptQueryWrapper.eq("company_id", eve.getId());
                    List<UserDept> userDepts = userDeptMapper.selectList(userDeptQueryWrapper);
                    List<String> idsDept= new ArrayList<>();
                    for (UserDept dept:userDepts) {
                        idsDept.add(dept.getDeptId());
                    }
                    if(CollectionUtils.isNotEmpty(idsDept)){
                        List<Department> departments = departmentMapper.selectBatchIds(idsDept);
                        eve.setDepts( departments);
                    }
                }
                item.setCompanys(list1);
            }


        }

        condition.setUserCompanys(list);
        long total = pageInfo.getTotal();
        condition.setRowCount(total);
    }



    private List<Company> getChildren(String id,List<String> ids) {
        QueryWrapper<Company> wrapper = new QueryWrapper<>();
        wrapper.eq("parent_id", id);
        List<Company> companies = companyMapper.selectList(wrapper);
        if(companies==null){
            return null;
        }
        for (Company item: companies ) {
            ids.add(item.getId());
            List<Company> children = this.getChildren(item.getId(),ids);
            if(children!=null){
                item.setChildren(children);
            }
        }
        return companies;
    }
}
