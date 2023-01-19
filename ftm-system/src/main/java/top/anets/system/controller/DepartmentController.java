package top.anets.system.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;
import top.anets.system.entity.Department;
import top.anets.system.service.DepartmentService;
import top.anets.utils.base.Result;

import java.util.List;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author ftm
 * @since 2022-02-14
 */
@RestController
@RequestMapping("/department")
public class DepartmentController {
    @Autowired
    private DepartmentService departmentService;

    @RequestMapping("/getDepts")
    public Result getDepts(String companyId){
        //获取当前用户所在的公司

        List<Department> depts = departmentService.querys(companyId);
        return  Result.success(depts);
    }


    @RequestMapping("/updateDept")
    public Result updateDept(@RequestBody Department department){
        boolean b = departmentService.saveOrUpdate(department);
        if(b){
            return  Result.success();
        }else{
            return  Result.error("失败");
        }

    }


    @RequestMapping("/deleteDept")
    public Result deleteDept(String departmentId){
        boolean b = departmentService.removeById(departmentId);
        if(b){
            return  Result.success();
        }else{
            return  Result.error("失败");
        }
    }
}

