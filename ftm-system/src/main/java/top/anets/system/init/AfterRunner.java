package top.anets.system.init;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.annotation.Order;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import top.anets.system.model.ResourceRoles;
import top.anets.system.service.SysRoleMenuService;
import top.anets.utils.redis.RedisConstant;

import java.util.*;

@Component
@Order(value = 1)
@Slf4j
public class AfterRunner implements ApplicationRunner {

    private Map<String, List<String>> resourceRolesMap;
    @Autowired
    private RedisTemplate  redisTemplate;
    @Autowired
    private SysRoleMenuService sysRoleMenuService;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        this.loadRoleResource();
    }

    private void loadRoleResource() {
        log.info("加载角色资源开始=====");
        resourceRolesMap = new TreeMap<>();
        List<ResourceRoles> res = sysRoleMenuService.loadResourceRoles();
        if(CollectionUtils.isEmpty(res)){
            return;
        }
        res.forEach(item->{
            resourceRolesMap.put(item.getResource(), item.getRoles());
        });


//        resourceRolesMap.put("/api/user/currentUser", Arrays.asList("ADMIN", "TEST"));
//        resourceRolesMap.put("/system/sys-user/getUser", Arrays.asList("ADMIN","SYSTEM:TEST"));
//        resourceRolesMap.put("/system/sys-menu/getList", Arrays.asList("ADMIN","SYSTEM:TEST"));
        redisTemplate.opsForHash().putAll(RedisConstant.RESOURCE_ROLES_MAP, resourceRolesMap);
//        Map<String,List<String>> resourceRoleMap = new TreeMap<>();
//        List<UmsResource> resourceList = resourceMapper.selectByExample(new UmsResourceExample());
//        List<UmsRole> roleList = roleMapper.selectByExample(new UmsRoleExample());
//        List<UmsRoleResourceRelation> relationList = roleResourceRelationMapper.selectByExample(new UmsRoleResourceRelationExample());
//        for (UmsResource resource : resourceList) {
//            Set<Long> roleIds = relationList.stream().filter(item -> item.getResourceId().equals(resource.getId())).map(UmsRoleResourceRelation::getRoleId).collect(Collectors.toSet());
//            List<String> roleNames = roleList.stream().filter(item -> roleIds.contains(item.getId())).map(item -> item.getId() + "_" + item.getName()).collect(Collectors.toList());
//            resourceRoleMap.put("/"+applicationName+resource.getUrl(),roleNames);
//        }
//        redisService.del(AuthConstant.RESOURCE_ROLES_MAP_KEY);
//        redisService.hSetAll(AuthConstant.RESOURCE_ROLES_MAP_KEY, resourceRoleMap);
//        return resourceRoleMap;
        log.info("加载角色资源结束=====");
    }
}