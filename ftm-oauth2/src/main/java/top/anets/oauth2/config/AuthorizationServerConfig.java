package top.anets.oauth2.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.ClientDetailsService;
import org.springframework.security.oauth2.provider.client.JdbcClientDetailsService;
import org.springframework.security.oauth2.provider.code.AuthorizationCodeServices;
import org.springframework.security.oauth2.provider.code.JdbcAuthorizationCodeServices;
import org.springframework.security.oauth2.provider.token.TokenEnhancer;
import org.springframework.security.oauth2.provider.token.TokenEnhancerChain;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.KeyStoreKeyFactory;

import javax.annotation.Resource;
import javax.sql.DataSource;
import java.security.KeyPair;
import java.util.ArrayList;
import java.util.List;

@Configuration
@EnableAuthorizationServer // 标识为认证服务器
public class AuthorizationServerConfig extends AuthorizationServerConfigurerAdapter {

    @Autowired
    private DataSource dataSource;

    @Bean // 客户端使用jdbc管理
    public ClientDetailsService jdbcClientDetailsService() {
        return new JdbcClientDetailsService(dataSource);
    }





    @Autowired // 在SpringSecurityConfig中已经添加到容器中了
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserDetailsService userDetailsService;

    @Resource
    private TokenStore tokenStore;

    @Resource
    private JwtAccessTokenConverter jwtAccessTokenConverter;

    @Resource // 注入增强器
    private TokenEnhancer jwtTokenEnhancer;
    /**
     * 关于认证服务器端点配置
     * @throws Exception
     */
//密码加密方式
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Override
    public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
        //配置 client信息从数据库中取
        clients.withClientDetails(jdbcClientDetailsService());
    }

    @Bean
    public AuthorizationCodeServices authorizationCodeServices() {
        // 授权码模式
        return new JdbcAuthorizationCodeServices(dataSource);
    }

    @Override
    public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
        // 密码模块必须使用这个authenticationManager实例
        endpoints.authenticationManager(authenticationManager);// 开启密码验证，由 WebSecurityConfigurerAdapter
        // 刷新令牌需要 使用userDetailsService
        endpoints.userDetailsService(userDetailsService);// 读取验证用户信息
        endpoints.setClientDetailsService(jdbcClientDetailsService());


        endpoints.authorizationCodeServices(authorizationCodeServices());
        //      授权模式配置自定义授权的接受入口
        endpoints.pathMapping("/oauth/confirm_access","/custom/confirm_access");
// 自定义异常跳转
        endpoints.pathMapping("/oauth/error", "/view/oauth/error");


        // 令牌管理方式
        endpoints.tokenStore(tokenStore).accessTokenConverter(jwtAccessTokenConverter);//token存储方式


        // 添加增强器,扩展器（客户端需要其他用户信息，则可以进行扩展）S============================
        TokenEnhancerChain enhancerChain = new TokenEnhancerChain();
        // 组合 增强器和jwt转换器
        List<TokenEnhancer> enhancerList = new ArrayList<>();
        enhancerList.add(jwtTokenEnhancer);
        enhancerList.add(jwtAccessTokenConverter);
        enhancerChain.setTokenEnhancers(enhancerList);
        // 将认证信息的增强器添加到端点上
        endpoints.tokenEnhancer(enhancerChain).accessTokenConverter(jwtAccessTokenConverter);
        // 添加增强器,扩展器（客户端需要其他用户信息，则可以进行扩展）E============================



    }

    @Override
    public void configure(AuthorizationServerSecurityConfigurer security) throws Exception {
        // /oauth/check_token 解析令牌，默认情况 下拒绝访问
//        security.checkTokenAccess("permitAll()");
        //  配置Endpoint,允许请求
        security.tokenKeyAccess("permitAll()") // 开启/oauth/token_key 验证端口-无权限
                .checkTokenAccess("isAuthenticated()") // 开启/oauth/check_token 验证端口-需权限
                .allowFormAuthenticationForClients()// 允许表单认证
                .passwordEncoder(passwordEncoder());   // 配置BCrypt加密
    }



}
