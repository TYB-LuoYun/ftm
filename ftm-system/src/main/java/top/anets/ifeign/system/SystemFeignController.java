package top.anets.ifeign.system;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;
import top.anets.system.entity.SysMenu;
import top.anets.system.entity.SysUser;
import top.anets.system.service.CompanyService;
import top.anets.system.service.SysUserService;

import java.util.ArrayList;
import java.util.List;


@Slf4j
@RestController
public class SystemFeignController implements  IFeignSystem{
    @Autowired
    private SysUserService sysUserService;


    @Autowired
    private CompanyService companyService;
    @Override
    public SysUser findByUsername(String username) {
        SysUser byUsername = sysUserService.findByUsername(username);



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
        menu.setCode("ADMIN");
        menu.setType(2);
        menu.setId("11111");
        List<SysMenu> menus = new ArrayList<>();
        menus.add(menu);
        log.info("返回模拟的菜单信息"+menus.toString());
        return menus;
    }


}
