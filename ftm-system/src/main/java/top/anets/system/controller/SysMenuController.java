package top.anets.system.controller;


import com.baomidou.mybatisplus.core.metadata.IPage;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import top.anets.base.PageQuery;
import top.anets.base.WrapperQuery;
import top.anets.system.entity.SysMenu;
import top.anets.system.service.SysMenuService;
import top.anets.system.vo.SysMenuVo;
import top.anets.utils.base.Result;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

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


    @ApiOperation(value = "新增/更新")
    @PostMapping("saveOrUpdate")
    public void saveOrUpdate(@RequestBody SysMenu sysMenu){
        sysMenuService.saveOrUpdate(sysMenu);
    }

    @ApiOperation(value = "新增/更新")
    @PostMapping("addOrModify")
    public void addOrModify(@RequestBody SysMenuVo sysMenuVo){
        //todo yourself
        sysMenuService.saveOrUpdate(WrapperQuery.from(sysMenuVo, SysMenu.class));
    }



    @ApiOperation(value = "删除")
    @RequestMapping("deletes")
    public void deletes(String... ids){
        sysMenuService.removeByIds(Arrays.asList(ids));
    }

    @ApiOperation(value = "Id查询")
    @GetMapping("/detail/{id}")
    public SysMenu findById(@PathVariable Long id){
        return sysMenuService.getById(id);
    }




    @ApiOperation(value = "查询-分页-查询和返回不处理")
    @RequestMapping("pages")
    public IPage pages(@RequestParam(required = false) Map<String,Object> params, PageQuery query){
        return sysMenuService.pages(WrapperQuery.query(params), query.Page());
    }


    @ApiOperation(value = "查询-分页-查询和返回新增字段或特殊处理")
    @RequestMapping("lists")
    public IPage lists(  SysMenuVo sysMenuVo, PageQuery query){
        IPage  pages = sysMenuService.pages(WrapperQuery.query(sysMenuVo), query.Page());
        WrapperQuery.ipage(pages,SysMenuVo.class).getRecords().forEach(item->{
            //         todo    item.get...

        });
        return pages;
    }







}

