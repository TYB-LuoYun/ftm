package top.anets.oauth2.controller;

import com.google.common.base.Preconditions;
import org.apache.commons.lang.StringUtils;
import org.apache.http.HttpHeaders;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.common.DefaultOAuth2AccessToken;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.ClientDetails;
import org.springframework.security.oauth2.provider.ClientDetailsService;
import org.springframework.security.oauth2.provider.endpoint.TokenEndpoint;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import top.anets.oauth2.config.AuthUtil;
import top.anets.oauth2.service.AuthService;
import top.anets.utils.base.RequestUtil;
import top.anets.utils.base.Result;

import javax.servlet.http.HttpServletRequest;
import java.security.Principal;
import java.util.LinkedHashMap;
import java.util.Map;


@RestController
public class AuthController {
    Logger logger = LoggerFactory.getLogger(getClass());

    private static final String HEADER_TYPE = "Basic ";

    @Autowired
    private ClientDetailsService clientDetailsService;
    @Autowired
    private TokenEndpoint tokenEndpoint;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private AuthService authService;

    @GetMapping("/user/refreshToken") // localhost:7001/auth/user/refreshToken?refreshToken=xxxx
    public Result refreshToken(HttpServletRequest request) {
        try {
            // 获取请求中的刷新令牌
            String refreshToken = request.getParameter("refreshToken");
            Preconditions.checkArgument(StringUtils.isNotEmpty(refreshToken), "刷新令牌不能为空");
            // 获取请求头
            String header = request.getHeader(HttpHeaders.AUTHORIZATION);
            if(header == null || !header.startsWith(HEADER_TYPE)) {
                throw new UnsupportedOperationException("请求头中无client信息");
            }
            // 解析请求头的客户端信息
            String[] tokens = RequestUtil.extractAndDecodeHeader(header);
            assert tokens.length == 2;

            String clientId = tokens[0];
            String clientSecret = tokens[1];

            // 查询客户端信息，核对是否有效
            ClientDetails clientDetails =
                    clientDetailsService.loadClientByClientId(clientId);
            if(clientDetails == null) {
                throw new UnsupportedOperationException("clientId对应的配置信息不存在：" + clientId);
            }
            // 校验客户端密码是否有效
            if( !passwordEncoder.matches(clientSecret, clientDetails.getClientSecret())) {
                throw new UnsupportedOperationException("无效clientSecret");
            }
            // 获取新的认证信息
            return authService.refreshToken(header, refreshToken);
        } catch(Exception e) {
            logger.error("refreshToken={}", e.getMessage(), e);
            return Result.error("新令牌获取失败：" + e.getMessage());
        }
    }


    /**
     * 自定义登入入口，需要去配置一下才能生效
     * @param principal
     * @param parameters
     * @return
     * @throws HttpRequestMethodNotSupportedException
     */
    @GetMapping("/oauth/token")
    public Result getAccessToken(Principal principal, @RequestParam Map<String, String> parameters) throws HttpRequestMethodNotSupportedException {
        return custom(tokenEndpoint.getAccessToken(principal, parameters).getBody());
    }

    @PostMapping("/oauth/token")
    public Result postAccessToken(Principal principal, @RequestParam Map<String, String> parameters) throws HttpRequestMethodNotSupportedException {
        return custom(tokenEndpoint.postAccessToken(principal, parameters).getBody());
    }


    //自定义返回格式
    private Result custom(OAuth2AccessToken accessToken) {
        DefaultOAuth2AccessToken token = (DefaultOAuth2AccessToken) accessToken;
        Map<String, Object> data = new LinkedHashMap(token.getAdditionalInformation());
        data.put("accessToken", token.getValue());
        if (token.getRefreshToken() != null) {
            data.put("refreshToken", token.getRefreshToken().getValue());
        }
        return Result.success(data);
    }


}
