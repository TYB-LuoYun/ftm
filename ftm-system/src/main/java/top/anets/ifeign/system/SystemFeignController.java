package top.anets.ifeign.system;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;
import top.anets.entity.system.Company;
import top.anets.entity.system.SysMenu;
import top.anets.entity.system.SysUser;
import top.anets.entity.system.UserCompany;
import top.anets.system.service.CompanyService;
import top.anets.system.service.SysUserService;
import top.anets.system.service.UserCompanyService;
import top.anets.vo.system.UserCompanyCondition;

import java.util.ArrayList;
import java.util.List;


@Slf4j
@RestController
public class SystemFeignController implements  IFeignSystem{
    @Autowired
    private SysUserService sysUserService;

    @Autowired
    private UserCompanyService userCompanyService;

    @Autowired
    private CompanyService companyService;
    @Override
    public SysUser findByUsername(String username) {
        SysUser byUsername = sysUserService.findByUsername(username);
        UserCompanyCondition condition = new UserCompanyCondition();
        condition.setUid(byUsername.getId());
        userCompanyService.querys(condition);
        List<UserCompany> userCompanys = condition.getUserCompanys();
        if(userCompanys==null||userCompanys.size()<=0){
            return byUsername;
        }

        Company company = null;
        for (UserCompany userCompany: userCompanys) {
            if(userCompany.getIsEnable()!=null&&userCompany.getIsEnable()==true){
                Company byId = companyService.getById(userCompany.getCompanyId());
                company=byId;
                break;
            }
        }
        if(company==null){
            UserCompany  userCompany = userCompanys.get(0);
            company=companyService.getById( userCompany.getCompanyId());
            userCompany.setIsEnable(true);
            userCompanyService.saveOrUpdate( userCompany);
        }

        byUsername.setCompany(company);
        return byUsername;
    }

    @Override
    public List<SysMenu> findByUserId(String userId) {
        log.info("接收到用户信息id"+userId);
        return this.mockAuthority();
    }


    /**
     * 模拟从数据库取权限
     * @return
     */
    private List<SysMenu> mockAuthority() {
        SysMenu menu = new SysMenu();
        menu.setCode("SYSTEM:TEST");
        menu.setType(2);
        menu.setId("11111");
        List<SysMenu> menus = new ArrayList<>();
        menus.add(menu);
        log.info("返回模拟的菜单信息"+menus.toString());
        return menus;
    }


}
