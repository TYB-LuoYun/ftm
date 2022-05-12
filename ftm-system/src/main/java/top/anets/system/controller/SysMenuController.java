package top.anets.system.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;
import top.anets.entity.system.SysMenu;
import top.anets.system.service.SysMenuService;
import top.anets.utils.base.Result;

import java.util.List;

/**
 * <p>
 * 菜单信息表 前端控制器
 * </p>
 *
 * @author ftm
 * @since 2021-07-26
 */
@RestController
@RequestMapping("/sys-menu")
public class SysMenuController {
    @Autowired
    private SysMenuService sysMenuService;

   @RequestMapping("getList")
   public Result getList(){
//       SysMenu sysMenu = new SysMenu();
//       sysMenu.setId("1");
//       sysMenu.setName("Root");
//       sysMenu.setPath("/");
//       Meta meta = new Meta();meta.setIcon("home-2-line");meta.setTitle("首页");meta.setBreadcrumbHidden(true);
//       sysMenu.setMeta(meta);
//       sysMenu.setComponent("Layout");
//
//
//
//       List<SysMenu> childs = new ArrayList<>();
//
//       SysMenu sysMenu1 = new SysMenu();
//       sysMenu1.setId("2");
//       sysMenu1.setName("Index");
//       sysMenu1.setPath("index");
//       Meta meta1 = new Meta();meta1.setIcon("home-2-line");meta1.setTitle("首页");meta1.setNoClosable(true);
//       sysMenu1.setMeta(meta1);
//       sysMenu1.setComponent("@/views/index");
//
//       SysMenu sysMenu2 = new SysMenu();
//       sysMenu2.setId("3");
//       sysMenu2.setName("Dashboard");
//       sysMenu2.setPath("dashboard");
//       Meta meta2 = new Meta();meta2.setIcon("dashboard-line");meta2.setTitle("看板");
//       sysMenu2.setMeta(meta2);
//       sysMenu2.setComponent("@/views/index/dashboard");
//
//       childs.add(sysMenu1);
//       childs.add(sysMenu2);
//       sysMenu.setChildren(childs);
//
//
//       ArrayList<SysMenu> parent = new ArrayList<>();
//       parent.add(sysMenu);
//


       List<SysMenu>  menus= sysMenuService.getRouterAllList();
       return Result.success(menus);

   }
}

