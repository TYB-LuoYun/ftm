package top.anets.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;
import top.anets.entity.system.Company;
import top.anets.entity.system.SysUser;
import top.anets.entity.system.UserCompany;
import top.anets.entity.system.UserDept;
import top.anets.system.mapper.*;
import top.anets.system.service.SysUserService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import top.anets.system.service.UserCompanyService;
import top.anets.vo.system.SysUserCondition;
import top.anets.vo.system.UserCompanyCondition;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 用户信息表 服务实现类
 * </p>
 *
 * @author ftm
 * @since 2021-07-26
 */
@RestController
@Slf4j
public class SysUserServiceImpl extends ServiceImpl<SysUserMapper, SysUser> implements SysUserService {
    @Autowired
    private UserCompanyService userCompanyService;

    @Autowired
    private SysUserMapper sysUserMapper;


    @Autowired
    private CompanyMapper companyMapper;


//    @Autowired
//    private DepartmentMapper departmentMapper;

    @Autowired
    private UserDeptMapper userDeptMapper;

    @Autowired
    private SysUserService userService;

    @Autowired
    private UserCompanyMapper userCompanyMapper;

    /**
     * 通过用户名查询用户信息
     *
     * @param username 用户名
     * @return
     */
    @Override
    public SysUser findByUsername(String username) {
        QueryWrapper<SysUser> wrapper = new QueryWrapper<>();
        wrapper.eq("username", username);
        return baseMapper.selectOne(wrapper);
    }

    @Override
    public void querys(SysUserCondition condition) {
        if(condition.getPageSize()==null){
            List<SysUser> list = sysUserMapper.querys(condition);
            condition.setItems(list);
            return;
        }
        log.info("页码："+condition.getPageNum()+";大小:"+condition.getPageSize());
        PageHelper.startPage(condition.getPageNum(),condition.getPageSize());
        List<SysUser> list = sysUserMapper.querys(condition);
        PageInfo<SysUser> pageInfo = new PageInfo<>(list);
        condition.setItems(list);
        long total = pageInfo.getTotal();
        condition.setRowCount(total);
    }

    @Override
    public void saveOrUpdateHandle(SysUser user) {

            //      更新用户信息
            userService.saveOrUpdate(user);


//      绑定用户与公司的管理
        QueryWrapper<UserCompany> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("company_id", user.getCompanyId());
        queryWrapper.eq("uid", user.getId());
        List<UserCompany> userCompanies = userCompanyMapper.selectList(queryWrapper);
        if(CollectionUtils.isEmpty(userCompanies)){//说明还没有被添加
            UserCompany userCompany = new UserCompany();
            userCompany.setUid(user.getId());
            userCompany.setCompanyId(user.getCompanyId());
            int insert = userCompanyMapper.insert(userCompany);
        }

//      绑定用户和部门的关系
        QueryWrapper<UserDept> wrapper = new QueryWrapper<>();
        wrapper.eq("dept_id", user.getDeptId());
        wrapper.eq("uid", user.getId());
        wrapper.eq("company_id", user.getCompanyId());
        userDeptMapper.delete(wrapper);

        UserDept userDept = new UserDept();
        userDept.setUid(user.getId());
        userDept.setDeptId(user.getDeptId());
        userDept.setCompanyId(user.getCompanyId());
        int insert = userDeptMapper.insert(userDept);
    }

    @Override
    public void querysDetail(SysUserCondition condition, SysUser userInfo) {
//        首先需要根据当前公司id，查出 当前公司 和  所有子公司的id，总的来说，还需要一个字段叫 母公司
        //      根据公司id查到子集
        List<String> ids = new ArrayList<>();
        ids.add(userInfo.getCompany().getId());
        List<Company>  a =this.getChildren(userInfo.getCompany().getId(),ids);

//        然后查出对应公司的用户id,要去重



//        根据用户id，还有用户id所在的公司id去查公司详情，部门详情

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
