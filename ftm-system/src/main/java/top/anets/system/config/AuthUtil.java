package top.anets.system.config;

import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.provider.authentication.OAuth2AuthenticationDetails;
import top.anets.system.entity.SysUser;

import java.util.Map;

@Slf4j
public class AuthUtil {

    public static SysUser getUserInfo() {
        Authentication authentication
                = SecurityContextHolder.getContext().getAuthentication();
        OAuth2AuthenticationDetails details =
                (OAuth2AuthenticationDetails)authentication.getDetails();
        Map<String, Object> map = (Map<String, Object>) details.getDecodedDetails();

        Map<String, Object>  userInfo = (Map<String, Object>) map.get("userInfo");
        String  userjson = JSONObject.toJSONString(userInfo);
        SysUser sysUser = JSONObject.parseObject(userjson, SysUser.class);


//        Set<Map.Entry<String, Object>> entries = userInfo.entrySet();
//        for (Map.Entry<String, Object>  item  : entries) {
//            log.info(item.getKey()+":"+item.getValue().toString());
//        }
//        SysUser user = new SysUser();
//        user.setId(userInfo.get("id").toString());
//        user.setNickName(userInfo.get("nickName").toString());
//        user.setUsername( userInfo.get("username") .toString());
//        user.setEmail( userInfo.get("email").toString() );
//        user.setImageUrl( userInfo.get("imageUrl") .toString());
//        user.setMobile( userInfo.get("mobile").toString());
//        user.setCompany((Company) userInfo.get("company"));

        return sysUser;
    }

    @ConfigurationProperties(prefix = "security.oauth2.client")
    public static class OAuth2ClientProperties {
        /**
         * OAuth2 client id.
         */
        private String clientId;

        /**
         * OAuth2 client secret. A random secret is generated by default.
         */
        private String clientSecret;
    }
}
