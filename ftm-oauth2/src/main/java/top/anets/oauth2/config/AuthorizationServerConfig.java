package top.anets.oauth2.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.redis.connection.RedisConnectionFactory;
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
import org.springframework.security.oauth2.provider.CompositeTokenGranter;
import org.springframework.security.oauth2.provider.TokenGranter;
import org.springframework.security.oauth2.provider.client.JdbcClientDetailsService;
import org.springframework.security.oauth2.provider.code.AuthorizationCodeServices;
import org.springframework.security.oauth2.provider.code.AuthorizationCodeTokenGranter;
import org.springframework.security.oauth2.provider.code.JdbcAuthorizationCodeServices;
import org.springframework.security.oauth2.provider.implicit.ImplicitTokenGranter;
import org.springframework.security.oauth2.provider.password.ResourceOwnerPasswordTokenGranter;
import org.springframework.security.oauth2.provider.refresh.RefreshTokenGranter;
import org.springframework.security.oauth2.provider.token.*;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.KeyStoreKeyFactory;
import top.anets.oauth2.mobileGrant.MobileCodeTokenGranter;
import top.anets.oauth2.service.customAuthCodeService.RedisAuthorizationCodeServices;
import top.anets.oauth2.service.customClient.ClientDetailsServiceImpl;

import javax.annotation.Resource;
import javax.sql.DataSource;
import java.security.KeyPair;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Configuration
@EnableAuthorizationServer // 标识为认证服务器
public class AuthorizationServerConfig extends AuthorizationServerConfigurerAdapter {

    @Autowired
    private DataSource dataSource;

    @Bean // 客户端使用jdbc管理
    public ClientDetailsService jdbcClientDetailsService() {
        return new JdbcClientDetailsService(dataSource);
    }


    @Autowired
    private RedisConnectionFactory redisConnectionFactory;


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

    @Autowired
    private ClientDetailsServiceImpl clientDetailsService;
    /**
     * 配置客户端详情
     * @param clients
     * @throws Exception
     */
    @Override
    public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
        //配置 client信息从数据库中取（自带）
//        clients.withClientDetails(jdbcClientDetailsService());
        //配置 client信息从数据库中取(我们自定义的)
        clients.withClientDetails(clientDetailsService);
    }

    /**
     * 配置授权码模式授权码服务,不配置默认为内存模式
     * @return
     */
    @Bean
    public AuthorizationCodeServices authorizationCodeServices() {
        // 授权码模式（自带）
//        return new JdbcAuthorizationCodeServices(dataSource);
        // 授权码模式（自定义redis生成）
        return new RedisAuthorizationCodeServices(redisConnectionFactory);
    }

    @Override
    public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
        // 1配置认证管理器 - 密码模块必须使用这个authenticationManager实例
        endpoints.authenticationManager(authenticationManager);// 开启密码验证，由 WebSecurityConfigurerAdapter
        // 2用户详情server，密码模式必须;刷新令牌需要 使用userDetailsService
        endpoints.userDetailsService(userDetailsService);// 读取验证用户信息
        // 3配置授权码模式授权码服务,不配置默认为内存模式
        endpoints.authorizationCodeServices(authorizationCodeServices());



        // 4令牌管理方式
        endpoints.tokenStore(tokenStore).accessTokenConverter(jwtAccessTokenConverter);//token存储方式

        // 5将认证信息的增强器添加到端点上,添加增强器,扩展器（客户端需要其他用户信息，则可以进行扩展）E============================
        endpoints.tokenEnhancer(enhancerChain()).accessTokenConverter(jwtAccessTokenConverter);

        // 6配置grant_type模式，如果不配置则默认使用密码模式、简化模式、验证码模式以及刷新token模式，如果配置了只使用配置中，默认配置失效
        // 具体可以查询AuthorizationServerEndpointsConfigurer中的getDefaultTokenGranters方法
        endpoints.tokenGranter(tokenGranter(endpoints));



        // 7配置TokenServices参数
        endpoints.tokenServices(tokenServices(endpoints));


        // 授权模式配置自定义授权的接受入口
        endpoints.pathMapping("/oauth/confirm_access","/custom/confirm_access");
        // 自定义异常跳转
        endpoints.pathMapping("/oauth/error", "/view/oauth/error");
    }

    private AuthorizationServerTokenServices tokenServices(AuthorizationServerEndpointsConfigurer endpoints) {
        DefaultTokenServices tokenServices = new DefaultTokenServices();
        tokenServices.setTokenStore(endpoints.getTokenStore());
        // 是否支持刷新Token
        tokenServices.setSupportRefreshToken(true);
        tokenServices.setReuseRefreshToken(true);
        tokenServices.setClientDetailsService(endpoints.getClientDetailsService());
        tokenServices.setTokenEnhancer(endpoints.getTokenEnhancer());
        // 设置accessToken和refreshToken的默认超时时间(如果clientDetails的为null就取默认的，如果clientDetails的不为null取clientDetails中的)
        tokenServices.setAccessTokenValiditySeconds((int) TimeUnit.HOURS.toSeconds(2));
        tokenServices.setRefreshTokenValiditySeconds((int) TimeUnit.DAYS.toSeconds(30));
        return tokenServices;
    }

    private TokenEnhancer enhancerChain() {
        // 添加增强器,扩展器（客户端需要其他用户信息，则可以进行扩展）S============================
        TokenEnhancerChain enhancerChain = new TokenEnhancerChain();
        // 组合 增强器和jwt转换器
        List<TokenEnhancer> enhancerList = new ArrayList<>();
        enhancerList.add(jwtTokenEnhancer);
        enhancerList.add(jwtAccessTokenConverter);
        enhancerChain.setTokenEnhancers(enhancerList);
        return enhancerChain;
    }

    @Override
    public void configure(AuthorizationServerSecurityConfigurer security) throws Exception {
        // /oauth/check_token 解析令牌，默认情况 下拒绝访问
//        security.checkTokenAccess("permitAll()");
        //  配置Endpoint,允许请求
        security.tokenKeyAccess("permitAll()") // 开启/oauth/token_key 验证端口-无权限
                .checkTokenAccess("isAuthenticated()") // 开启/oauth/check_token 验证端口-需权限
                .allowFormAuthenticationForClients()// 允许表单认证
                .passwordEncoder(passwordEncoder())
                ;
    }


    /**
     * 创建grant_type列表
     * @param endpoints
     * @return
     */
    private TokenGranter tokenGranter(AuthorizationServerEndpointsConfigurer endpoints) {
        List<TokenGranter> list = new ArrayList<>();
        // 这里配置密码模式、刷新token模式、自定义手机号验证码模式、授权码模式、简化模式
        list.add(new ResourceOwnerPasswordTokenGranter(authenticationManager, endpoints.getTokenServices(), endpoints.getClientDetailsService(), endpoints.getOAuth2RequestFactory()));
        list.add(new RefreshTokenGranter(endpoints.getTokenServices(), endpoints.getClientDetailsService(), endpoints.getOAuth2RequestFactory()));
        /**
         * 自定义手机验证码登录
         */
        list.add(new MobileCodeTokenGranter(authenticationManager,endpoints.getTokenServices(), endpoints.getClientDetailsService(), endpoints.getOAuth2RequestFactory()));

        list.add(new AuthorizationCodeTokenGranter(endpoints.getTokenServices(),endpoints.getAuthorizationCodeServices(), endpoints.getClientDetailsService(), endpoints.getOAuth2RequestFactory()));
        list.add(new ImplicitTokenGranter(endpoints.getTokenServices(),endpoints.getClientDetailsService(),endpoints.getOAuth2RequestFactory()));
        return new CompositeTokenGranter(list);
    }




}
