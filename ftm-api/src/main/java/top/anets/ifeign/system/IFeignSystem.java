package top.anets.ifeign.system;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import top.anets.entity.system.SysMenu;
import top.anets.entity.system.SysUser;

import java.util.List;

//path需要与content-path
@FeignClient(value="system-server", path = "/system")
public interface IFeignSystem {
    @RequestMapping("/feign/sysUser/findByUsername")
    SysUser findByUsername(@RequestParam("username") String username);


    @RequestMapping("/feign/sysMenu/findByUserId")
    List<SysMenu> findByUserId(@RequestParam("userId") String userId);
}
