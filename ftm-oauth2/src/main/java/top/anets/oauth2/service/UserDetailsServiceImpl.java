package top.anets.oauth2.service;
import com.netflix.client.ClientException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import top.anets.system.entity.SysMenu;
import top.anets.system.entity.SysUser;
import top.anets.ifeign.system.IFeignSystem;

import java.util.ArrayList;
import java.util.List;
@Slf4j
@Service // 不一定不要少了
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private IFeignSystem iFeignSystem;

    @Autowired
    private IFeignSystem IFeignSystem;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // 1. 判断用户名是否为空
        if(StringUtils.isEmpty(username)) {
            throw new BadCredentialsException("用户名不能为空");
        }
        log.info("参数username："+username);
        // 2. 通过用户名查询数据库中的用户信息
        SysUser sysUser = iFeignSystem.findByUsername(username);
        log.info(sysUser.toString());
        if(sysUser == null) {
            throw new BadCredentialsException("用户名或密码错误");
        }

        // 3. 通过用户id去查询数据库的拥有的权限信息
        log.info("通过用户id去查询数据库的拥有的权限信息:"+sysUser.getId());
        List<SysMenu> menuList =
                IFeignSystem.findByUserId(sysUser.getId());

        // 4. 封装权限信息（权限标识符code）
        log.info("封装角色信息");
        List<GrantedAuthority> authorities = null;
        if(CollectionUtils.isNotEmpty(menuList)) {
            authorities = new ArrayList<>();
            for(SysMenu menu: menuList) {
                // 权限标识
                String code = menu.getCode();
                log.info("角色："+code);
                authorities.add(new SimpleGrantedAuthority(code));
            }
        }else{
            log.info("无角色信息");
        }

        log.info("user对象转移");
        JwtUser jwtUser = new JwtUser();
        BeanUtils.copyProperties(sysUser, jwtUser);
        jwtUser.setCompany(sysUser.getCompany());


        // 5. 构建UserDetails接口的实现类JwtUser对象
        jwtUser.setAuthorities(authorities);

//        JwtUser jwtUser = new JwtUser( sysUser.getId(), sysUser.getUsername(), sysUser.getPassword(),
//                sysUser.getNickName(), sysUser.getImageUrl(), sysUser.getMobile(), sysUser.getEmail(),
//                sysUser.getIsAccountNonExpired(), sysUser.getIsAccountNonLocked(),
//                sysUser.getIsCredentialsNonExpired(), sysUser.getIsEnabled(),
//                authorities );
        log.info("jwtuser:"+jwtUser.toString());
        return jwtUser;
    }

    public UserDetails loadUserByMobile(String mobile) {
        log.info("手机号模式查询用户信息");
        SysUser sysUser = new SysUser( );
        if (sysUser == null) {
            throw new UsernameNotFoundException("not found mobile user:" + mobile);
        }
        JwtUser userDetail = new JwtUser();
        BeanUtils.copyProperties(sysUser, userDetail);
        BeanUtils.copyProperties(sysUser,userDetail);
        userDetail.setAuthorities(new ArrayList<>());
        return userDetail;
    }
}
