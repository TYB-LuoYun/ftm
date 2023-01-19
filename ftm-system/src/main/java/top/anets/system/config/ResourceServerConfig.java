package top.anets.system.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.TokenStore;

@EnableGlobalMethodSecurity(prePostEnabled = true) // 开启方法级别权限控制
@EnableResourceServer // 标识为资源服务器，请求资源接口时，必须在请求头带个access_token
@Configuration
public class ResourceServerConfig extends ResourceServerConfigurerAdapter {

    @Autowired
    private TokenStore tokenStore;

    @Override
    public void configure(ResourceServerSecurityConfigurer resources) throws Exception {
        resources.tokenStore(tokenStore); // jwt管理令牌
    }

    /**
     * 资源服务器的安全配置
     * @param http
     * @throws Exception
     */
    @Override
    public void configure(HttpSecurity http) throws Exception {
        http.sessionManagement() // 采用token进行管理身份，而没有采用session，所以不需要创建HttpSession
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            .and()
                .authorizeRequests() // 请求的授权配置
                // 将 swagger接口文档相关的url放行
                .antMatchers("/v2/api-docs", "/v2/feign-docs",
                        "/swagger-resources/configuration/ui",
                        "/swagger-resources","/swagger-resources/configuration/security",
                        "/swagger-ui.html", "/webjars/**").permitAll()
                // 放行以 /sysUserFeign 开头的请求接口 - 开放远程调用的接口
                .antMatchers("/feign/**").permitAll()
                // 放行 公共api
                .antMatchers("/api/**","/sys-user/login").permitAll()
                .antMatchers("/api/**","/login").permitAll()
                .antMatchers("/assets/**").permitAll()
                .antMatchers(   "/login" ).permitAll()
                .antMatchers(  "/instances/**").permitAll()
                .antMatchers(  "/actuator/**").permitAll()
                // 所有请求都要有all范围权限
                .antMatchers("/**").access("#oauth2.hasScope('all')")
                // 其他请求都要通过身份认证
                .anyRequest().authenticated();
    }
}
