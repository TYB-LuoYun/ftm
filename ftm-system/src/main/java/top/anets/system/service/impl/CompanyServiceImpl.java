package top.anets.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import top.anets.entity.system.Company;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import top.anets.entity.system.SysUser;
import top.anets.entity.system.UserCompany;
import top.anets.system.mapper.CompanyMapper;
import top.anets.system.service.CompanyService;
import top.anets.vo.system.CompanyVo;

import java.util.ArrayList;
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
public class CompanyServiceImpl extends ServiceImpl<CompanyMapper, Company> implements CompanyService {
    @Autowired
    private CompanyMapper companyMapper;


    @Override
    public List<Company> getSelfAndChildren(String companyId) {
        ArrayList<Company> companies = new ArrayList<>();
        List<Company> children = this.getChildren(companyId, companies);
        Company company = companyMapper.selectById(companyId);
        children.add(company);
        return children;
    }

    @Override
    public void querys(CompanyVo condition) {
        PageHelper.startPage(condition.getPageNum(),condition.getPageSize());
        List<Company> list = companyMapper.querys(condition);
        PageInfo<Company> pageInfo = new PageInfo<>(list);
        condition.setItems(list);
        long total = pageInfo.getTotal();
        condition.setRowCount(total);
    }


    private List<Company> getChildren(String id,List<Company> ids) {
        QueryWrapper<Company> wrapper = new QueryWrapper<>();
        wrapper.eq("parent_id", id);
        List<Company> companies = companyMapper.selectList(wrapper);
        if(companies==null){
            return null;
        }
        for (Company item: companies ) {
            ids.add(item);
            List<Company> children = this.getChildren(item.getId(),ids);
            if(children!=null){
                item.setChildren(children);
            }
        }
        return companies;
    }
}
