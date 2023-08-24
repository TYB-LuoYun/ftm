package top.anets.oauth2.controller;

import com.google.common.base.Preconditions;
import org.apache.commons.lang.StringUtils;
import org.apache.http.HttpHeaders;
import org.checkerframework.checker.units.qual.A;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.common.DefaultOAuth2AccessToken;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.common.exceptions.*;
import org.springframework.security.oauth2.common.util.OAuth2Utils;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerEndpointsConfiguration;
import org.springframework.security.oauth2.provider.*;
import org.springframework.security.oauth2.provider.approval.DefaultUserApprovalHandler;
import org.springframework.security.oauth2.provider.approval.UserApprovalHandler;
import org.springframework.security.oauth2.provider.code.AuthorizationCodeServices;
import org.springframework.security.oauth2.provider.code.InMemoryAuthorizationCodeServices;
import org.springframework.security.oauth2.provider.endpoint.*;
import org.springframework.security.oauth2.provider.implicit.ImplicitGrantService;
import org.springframework.security.oauth2.provider.implicit.ImplicitTokenRequest;
import org.springframework.security.oauth2.provider.request.DefaultOAuth2RequestValidator;
import org.springframework.ui.Model;
import org.springframework.util.ObjectUtils;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.support.SessionAttributeStore;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.View;
import org.springframework.web.servlet.view.RedirectView;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;
import top.anets.oauth2.config.AuthUtil;
import top.anets.oauth2.service.AuthService;
import top.anets.oauth2.service.customClient.ClientDetailsServiceImpl;
import top.anets.utils.base.HttpClientUtil;
import top.anets.utils.base.RequestUtil;
import top.anets.utils.base.Result;
import top.anets.utils.base.ServletUtil;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.net.URI;
import java.security.Principal;
import java.util.*;


@RestController
//这个是为了重写授权码验证
@SessionAttributes({"authorizationRequest", "org.springframework.security.oauth2.provider.endpoint.AuthorizationEndpoint.ORIGINAL_AUTHORIZATION_REQUEST"})
public class AuthController  extends AbstractEndpoint {
    Logger logger = LoggerFactory.getLogger(getClass());

    private static final String HEADER_TYPE = "Basic ";

    @Autowired
    private ClientDetailsService clientDetailsService;

    @Autowired
    private AuthenticationManager authenticationManager;
    /**
     * oauth2配置类，获取到实例，用于父类实例化配置需要
     */
    @Resource
    private AuthorizationServerEndpointsConfiguration configuration;


    @Autowired
    private AuthorizationCodeServices authorizationCodeServices  ;


    @Autowired
    private TokenEndpoint tokenEndpoint;
    @Autowired
    private PasswordEncoder passwordEncoder;

    private RedirectResolver redirectResolver = new DefaultRedirectResolver();

    private OAuth2RequestValidator oauth2RequestValidator = new DefaultOAuth2RequestValidator();

    private UserApprovalHandler userApprovalHandler = new DefaultUserApprovalHandler() ;



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


    /**
     * Spring Oauth2实现登录同时授权、自定义授权界面
     * @param parameters
     * @param sessionStatus
     * @return
     */
    @RequestMapping("/oauth/authorize")
public ModelAndView authorize(@RequestParam Map<String, String> parameters,
                               SessionStatus sessionStatus ) {

    // Pull out the authorization request first, using the OAuth2RequestFactory. All further logic should
    // query off of the authorization request instead of referring back to the parameters map. The contents of the
    // parameters map will be stored without change in the AuthorizationRequest object once it is created.
    // 参数处理
    AuthorizationRequest authorizationRequest = getOAuth2RequestFactory().createAuthorizationRequest(parameters);
    // 请求类型，应该是code或者token
    Set<String> responseTypes = authorizationRequest.getResponseTypes();

    System.out.println(responseTypes);
    if (!responseTypes.contains("token") && !responseTypes.contains("code")) {
        throw new UnsupportedResponseTypeException("不支持的响应类型: " + responseTypes);
    }

    if (authorizationRequest.getClientId() == null) {
        throw new InvalidClientException("必须提供客户端id");
    }

    try {
        // 不需要授权
//            if (!(principal instanceof Authentication) || !((Authentication) principal).isAuthenticated()) {
//                throw new InsufficientAuthenticationException(
//                    "User must be authenticated with Spring Security before authorization can be completed.");
//            }
        ClientDetails client = this.getClientDetailsService().loadClientByClientId(authorizationRequest.getClientId());
        System.out.println(client);
        String redirectUriParameter = (String)authorizationRequest.getRequestParameters().get("redirect_uri");
        // 检查客户端是否支持隐式授权或者code模式，从客户端中获取可以跳转的url列表，将url与其进行比较校验，如果
        // 不支持，或者不在url列表中存在则抛出异常
        String resolvedRedirect = this.redirectResolver.resolveRedirect(redirectUriParameter, client);
        if (!org.springframework.util.StringUtils.hasText(resolvedRedirect)) {
            throw new RedirectMismatchException("redirectUri 必须在 ClientDetailsi 中提供或预先配置");
        }
        authorizationRequest.setRedirectUri(resolvedRedirect);
        if (!client.isScoped()) {
            throw new InvalidScopeException(String.format("客户端 %s 不可用", client.getClientId()));
        }
        // 验证客户端请求的授权范围是否在客户端支持的授权范围之内
        oauth2RequestValidator.validateScope(authorizationRequest, client);


//        // 此处不进行自动授权，跳过这个步骤，而且前面也没有对用户是否登陆进行判断
//        // 同时此处不重定向到什么登陆界面，直接展示登陆界面
//        System.out.println(client);
//        Set<String> mustApprove = client.getMustapprove();
//        LinkedList<Map<String, Object>> scopes = new LinkedList<>();
//        for (String scope :authorizationRequest.getScope()) {
//            ScopeEnum scopeEnum = ScopeEnum.valueOf(scope);
//            boolean must = mustApprove.contains(scope);
//            Map<String, Object> map = new HashMap<String, Object>(){{
//                put("type", scopeEnum.name());
//                put("info", scopeEnum.getInfo());
//                put("must", must);
//            }};
//            // 必须的范围放在最后面
//            if (must) {
//                scopes.addLast(map);
//            } else {
//                scopes.addFirst(map);
//            }
//        }
//        model.addAttribute("clientName", client.getClientName());
//        model.addAttribute("scopes", scopes);
//
//        // Store authorizationRequest AND an immutable Map of authorizationRequest in session
//        // which will be used to validate against in approveOrDeny()
//        model.addAttribute(AUTHORIZATION_REQUEST_ATTR_NAME, authorizationRequest);
//        model.addAttribute(ORIGINAL_AUTHORIZATION_REQUEST_ATTR_NAME, unmodifiableMap(authorizationRequest));
        ModelAndView view = new ModelAndView();
        view.setViewName("login-authorize");
        view.addObject("clientId", authorizationRequest.getClientId());
        view.addObject("scopes",authorizationRequest.getScope());
        view.addObject("authorizationRequest", authorizationRequest);
        view.addObject("org.springframework.security.oauth2.provider.endpoint.AuthorizationEndpoint.ORIGINAL_AUTHORIZATION_REQUEST", unmodifiableMap(authorizationRequest));
        return view;
    } catch (RuntimeException e) {
        sessionStatus.setComplete();
        throw e;
    }
}



@Autowired
private AuthorizationEndpoint authorizationEndpoint;


    @RequestMapping(
            value = {"/oauth/authorize"},
            method = {RequestMethod.POST},
            params = {"user_oauth_approval"}
    )
    public View approveOrDeny(@RequestParam Map<String, String> approvalParameters, Map<String, ?> model, SessionStatus sessionStatus, Principal principal) {

        /**
         * 没有登录先进行登录
         */
        if (!(principal instanceof Authentication)) {
            UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(approvalParameters.get("username"), approvalParameters.get("password"));
            Authentication authentication = authenticationManager.authenticate(usernamePasswordAuthenticationToken);
            SecurityContext securityContext = SecurityContextHolder.getContext();
            securityContext.setAuthentication(authentication);
            principal = authentication;
        }

        AuthorizationRequest authorizationRequest = (AuthorizationRequest)model.get("authorizationRequest");
        if (authorizationRequest == null) {
            sessionStatus.setComplete();
            throw new InvalidRequestException("Cannot approve uninitialized authorization request.");
        } else {
            Map<String, Object> originalAuthorizationRequest = (Map)model.get("org.springframework.security.oauth2.provider.endpoint.AuthorizationEndpoint.ORIGINAL_AUTHORIZATION_REQUEST");
            if (this.isAuthorizationRequestModified(authorizationRequest, originalAuthorizationRequest)) {
                throw new InvalidRequestException("Changes were detected from the original authorization request.");
            } else {
                View var9;
                try {
                    Set<String> responseTypes = authorizationRequest.getResponseTypes();
                    authorizationRequest.setApprovalParameters(approvalParameters);
                    authorizationRequest = this.userApprovalHandler.updateAfterApproval(authorizationRequest, (Authentication)principal);
                    boolean approved = this.userApprovalHandler.isApproved(authorizationRequest, (Authentication)principal);
                    authorizationRequest.setApproved(approved);
                    if (authorizationRequest.getRedirectUri() == null) {
                        sessionStatus.setComplete();
                        throw new InvalidRequestException("Cannot approve request when no redirect URI is provided.");
                    }

                    if (!authorizationRequest.isApproved()) {
                        RedirectView var13 = new RedirectView(this.getUnsuccessfulRedirect(authorizationRequest, new UserDeniedAuthorizationException("User denied access"), responseTypes.contains("token")), false, true, false);
                        return var13;
                    }

                    if (responseTypes.contains("token")) {
                        var9 = this.getImplicitGrantResponse(authorizationRequest).getView();
                        return var9;
                    }

                    var9 = this.getAuthorizationCodeResponse(authorizationRequest, (Authentication)principal);
                } finally {
                    sessionStatus.setComplete();
                }

                return var9;
            }
        }
    }


    private ModelAndView getImplicitGrantResponse(AuthorizationRequest authorizationRequest) {
        try {
            TokenRequest tokenRequest = this.getOAuth2RequestFactory().createTokenRequest(authorizationRequest, "implicit");
            OAuth2Request storedOAuth2Request = this.getOAuth2RequestFactory().createOAuth2Request(authorizationRequest);
            OAuth2AccessToken accessToken = this.getAccessTokenForImplicitGrant(tokenRequest, storedOAuth2Request);
            if (accessToken == null) {
                throw new UnsupportedResponseTypeException("Unsupported response type: token");
            } else {
                return new ModelAndView(new RedirectView(this.appendAccessToken(authorizationRequest, accessToken), false, true, false));
            }
        } catch (OAuth2Exception var5) {
            return new ModelAndView(new RedirectView(this.getUnsuccessfulRedirect(authorizationRequest, var5, true), false, true, false));
        }
    }

    private Object implicitLock = new Object();
    private OAuth2AccessToken getAccessTokenForImplicitGrant(TokenRequest tokenRequest, OAuth2Request storedOAuth2Request) {
        OAuth2AccessToken accessToken = null;
        Object var4 = this.implicitLock;
        synchronized(this.implicitLock) {
            accessToken = this.getTokenGranter().grant("implicit", new ImplicitTokenRequest(tokenRequest, storedOAuth2Request));
            return accessToken;
        }
    }


    private String appendAccessToken(AuthorizationRequest authorizationRequest, OAuth2AccessToken accessToken) {
        Map<String, Object> vars = new LinkedHashMap();
        Map<String, String> keys = new HashMap();
        if (accessToken == null) {
            throw new InvalidRequestException("An implicit grant could not be made");
        } else {
            vars.put("access_token", accessToken.getValue());
            vars.put("token_type", accessToken.getTokenType());
            String state = authorizationRequest.getState();
            if (state != null) {
                vars.put("state", state);
            }

            Date expiration = accessToken.getExpiration();
            if (expiration != null) {
                long expires_in = (expiration.getTime() - System.currentTimeMillis()) / 1000L;
                vars.put("expires_in", expires_in);
            }

            String originalScope = (String)authorizationRequest.getRequestParameters().get("scope");
            if (originalScope == null || !OAuth2Utils.parseParameterList(originalScope).equals(accessToken.getScope())) {
                vars.put("scope", OAuth2Utils.formatParameterList(accessToken.getScope()));
            }

            Map<String, Object> additionalInformation = accessToken.getAdditionalInformation();
            Iterator var9 = additionalInformation.keySet().iterator();

            while(var9.hasNext()) {
                String key = (String)var9.next();
                Object value = additionalInformation.get(key);
                if (value != null) {
                    keys.put("extra_" + key, key);
                    vars.put("extra_" + key, value);
                }
            }

            return this.append(authorizationRequest.getRedirectUri(), vars, keys, true);
        }
    }


    private boolean isAuthorizationRequestModified(AuthorizationRequest authorizationRequest, Map<String, Object> originalAuthorizationRequest) {
        if (!ObjectUtils.nullSafeEquals(authorizationRequest.getClientId(), originalAuthorizationRequest.get("client_id"))) {
            return true;
        } else if (!ObjectUtils.nullSafeEquals(authorizationRequest.getState(), originalAuthorizationRequest.get("state"))) {
            return true;
        } else if (!ObjectUtils.nullSafeEquals(authorizationRequest.getRedirectUri(), originalAuthorizationRequest.get("redirect_uri"))) {
            return true;
        } else if (!ObjectUtils.nullSafeEquals(authorizationRequest.getResponseTypes(), originalAuthorizationRequest.get("response_type"))) {
            return true;
        } else if (!ObjectUtils.nullSafeEquals(authorizationRequest.getScope(), originalAuthorizationRequest.get("scope"))) {
            return true;
        } else if (!ObjectUtils.nullSafeEquals(authorizationRequest.isApproved(), originalAuthorizationRequest.get("approved"))) {
            return true;
        } else if (!ObjectUtils.nullSafeEquals(authorizationRequest.getResourceIds(), originalAuthorizationRequest.get("resourceIds"))) {
            return true;
        } else {
            return !ObjectUtils.nullSafeEquals(authorizationRequest.getAuthorities(), originalAuthorizationRequest.get("authorities"));
        }
    }

    Map<String, Object> unmodifiableMap(AuthorizationRequest authorizationRequest) {
        Map<String, Object> authorizationRequestMap = new HashMap();
        authorizationRequestMap.put("client_id", authorizationRequest.getClientId());
        authorizationRequestMap.put("state", authorizationRequest.getState());
        authorizationRequestMap.put("redirect_uri", authorizationRequest.getRedirectUri());
        if (authorizationRequest.getResponseTypes() != null) {
            authorizationRequestMap.put("response_type", Collections.unmodifiableSet(new HashSet(authorizationRequest.getResponseTypes())));
        }

        if (authorizationRequest.getScope() != null) {
            authorizationRequestMap.put("scope", Collections.unmodifiableSet(new HashSet(authorizationRequest.getScope())));
        }

        authorizationRequestMap.put("approved", authorizationRequest.isApproved());
        if (authorizationRequest.getResourceIds() != null) {
            authorizationRequestMap.put("resourceIds", Collections.unmodifiableSet(new HashSet(authorizationRequest.getResourceIds())));
        }

        if (authorizationRequest.getAuthorities() != null) {
            authorizationRequestMap.put("authorities", Collections.unmodifiableSet(new HashSet(authorizationRequest.getAuthorities())));
        }

        return Collections.unmodifiableMap(authorizationRequestMap);
    }

    /**
     * 初始化时被调用，传入客户端服务,这个很重要
     * @throws Exception
     */
    @Override
    public void afterPropertiesSet() throws Exception {
        setClientDetailsService( clientDetailsService);
        setTokenGranter(configuration.getEndpointsConfigurer().getTokenGranter());
        userApprovalHandler = configuration.getEndpointsConfigurer().getUserApprovalHandler();
        super.afterPropertiesSet();
    }

    private View getAuthorizationCodeResponse(AuthorizationRequest authorizationRequest, Authentication authUser) {
        try {
            return new RedirectView(this.getSuccessfulRedirect(authorizationRequest, this.generateCode(authorizationRequest, authUser)), false, true, false);
        } catch (OAuth2Exception var4) {
            return new RedirectView(this.getUnsuccessfulRedirect(authorizationRequest, var4, false), false, true, false);
        }
    }

    private String getSuccessfulRedirect(AuthorizationRequest authorizationRequest, String authorizationCode) {
        if (authorizationCode == null) {
            throw new IllegalStateException("No authorization code found in the current request scope.");
        } else {
            Map<String, String> query = new LinkedHashMap();
            query.put("code", authorizationCode);
            String state = authorizationRequest.getState();
            if (state != null) {
                query.put("state", state);
            }

            return this.append(authorizationRequest.getRedirectUri(), query, false);
        }
    }

    private String getUnsuccessfulRedirect(AuthorizationRequest authorizationRequest, OAuth2Exception failure, boolean fragment) {
        if (authorizationRequest != null && authorizationRequest.getRedirectUri() != null) {
            Map<String, String> query = new LinkedHashMap();
            query.put("error", failure.getOAuth2ErrorCode());
            query.put("error_description", failure.getMessage());
            if (authorizationRequest.getState() != null) {
                query.put("state", authorizationRequest.getState());
            }

            if (failure.getAdditionalInformation() != null) {
                Iterator var5 = failure.getAdditionalInformation().entrySet().iterator();

                while(var5.hasNext()) {
                    Map.Entry<String, String> additionalInfo = (Map.Entry)var5.next();
                    query.put(additionalInfo.getKey(), additionalInfo.getValue());
                }
            }

            return this.append(authorizationRequest.getRedirectUri(), query, fragment);
        } else {
            throw new UnapprovedClientAuthenticationException("Authorization failure, and no redirect URI.", failure);
        }
    }


    private String generateCode(AuthorizationRequest authorizationRequest, Authentication authentication) throws AuthenticationException {
        try {
            OAuth2Request storedOAuth2Request = this.getOAuth2RequestFactory().createOAuth2Request(authorizationRequest);
            OAuth2Authentication combinedAuth = new OAuth2Authentication(storedOAuth2Request, authentication);
            String code = this.authorizationCodeServices.createAuthorizationCode(combinedAuth);
            return code;
        } catch (OAuth2Exception var6) {
            if (authorizationRequest.getState() != null) {
                var6.addAdditionalInformation("state", authorizationRequest.getState());
            }

            throw var6;
        }
    }

    private String append(String base, Map<String, ?> query, boolean fragment) {
        return this.append(base, query, (Map)null, fragment);
    }

    private String append(String base, Map<String, ?> query, Map<String, String> keys, boolean fragment) {
        UriComponentsBuilder template = UriComponentsBuilder.newInstance();
        UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(base);

        URI redirectUri;
        try {
            redirectUri = builder.build(true).toUri();
        } catch (Exception var12) {
            redirectUri = builder.build().toUri();
            builder = UriComponentsBuilder.fromUri(redirectUri);
        }

        template.scheme(redirectUri.getScheme()).port(redirectUri.getPort()).host(redirectUri.getHost()).userInfo(redirectUri.getUserInfo()).path(redirectUri.getPath());
        String key;
        if (fragment) {
            StringBuilder values = new StringBuilder();
            if (redirectUri.getFragment() != null) {
                key = redirectUri.getFragment();
                values.append(key);
            }

            String name;
            for(Iterator var15 = query.keySet().iterator(); var15.hasNext(); values.append(name + "={" + key + "}")) {
                key = (String)var15.next();
                if (values.length() > 0) {
                    values.append("&");
                }

                name = key;
                if (keys != null && keys.containsKey(key)) {
                    name = (String)keys.get(key);
                }
            }

            if (values.length() > 0) {
                template.fragment(values.toString());
            }

            UriComponents encoded = template.build().expand(query).encode();
            builder.fragment(encoded.getFragment());
        } else {
            for(Iterator var13 = query.keySet().iterator(); var13.hasNext(); template.queryParam(key, new Object[]{"{" + key + "}"})) {
                key = (String)var13.next();
                key = key;
                if (keys != null && keys.containsKey(key)) {
                    key = (String)keys.get(key);
                }
            }

            template.fragment(redirectUri.getFragment());
            UriComponents encoded = template.build().expand(query).encode();
            builder.query(encoded.getQuery());
        }

        return builder.build().toUriString();
    }


}
