package top.anets.system.controller;


import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import top.anets.common.utils.base.Result;

@RestController
@RequestMapping("/test")
public class TestController {
    /**
     * 测试资源权限
     */
    @PreAuthorize("hasAnyAuthority('SYSTEM:TEST')")
    @RequestMapping("/judgeAuthority")
    public Result judgeAuthority(){
        return Result.success("访问正常，拥有测试权限");
    }


    /**
     * 测试资源权限
     */
    @RequestMapping("/noAuthority")
    public Result noAuthority(){
        return Result.success("访问正常，不需要权限");
    }
}
