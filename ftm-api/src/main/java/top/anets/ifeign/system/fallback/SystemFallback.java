package top.anets.ifeign.system.fallback;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import top.anets.ifeign.system.IFeignSystem;
import top.anets.system.entity.SysMenu;
import top.anets.system.entity.SysUser;

import java.util.List;

/**
 * @author ftm
 * @date 2023-08-24 15:35
 */
@Service
@Slf4j
public class SystemFallback implements IFeignSystem {
    @Override
    public SysUser findByUsername(String username) {
        log.info("服务降级了");
        return null;
    }

    @Override
    public List<SysMenu> findByUserId(String userId) {
        log.info("服务降级了");
        return null;
    }

    @Override
    public SysUser findByPhone(String mobile) {
        return null;
    }
}
