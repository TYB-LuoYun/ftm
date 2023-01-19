package top.anets.ifeign.system;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import top.anets.system.entity.SysMenu;
import top.anets.system.entity.SysUser;

import java.util.List;

//path需要与content-path
@FeignClient(value="system-server")
public interface IFeignSystem {
    @RequestMapping("/feign/sysUser/findByUsername")
    SysUser findByUsername(@RequestParam("username") String username);


    @RequestMapping("/feign/sysMenu/findByUserId")
    List<SysMenu> findByUserId(@RequestParam("userId") String userId);
}
