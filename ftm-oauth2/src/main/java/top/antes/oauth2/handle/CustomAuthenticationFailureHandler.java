//package top.antes.oauth2.handle;
//
//import com.fasterxml.jackson.databind.ObjectMapper;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.security.core.AuthenticationException;
//import org.springframework.security.static.authentication.AuthenticationFailureHandler;
//import top.anets.utils.base.Result;
//
//import javax.servlet.ServletException;
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
//import java.io.IOException;
//
///**
// * 失败处理器：认证失败后响应json给前端
// */
//@Slf4j
//public class CustomAuthenticationFailureHandler implements AuthenticationFailureHandler {
//
//    @Autowired
//    private ObjectMapper objectMapper;
//
//    @Override
//    public void onAuthenticationFailure(HttpServletRequest request,
//                                        HttpServletResponse response,
//                                        AuthenticationException e) throws IOException, ServletException {
//        log.info("进入失败处理器==============================");
//        // 响应错误信息：json格式
//        response.setContentType("application/json;charset=UTF-8");
//        String result = objectMapper.writeValueAsString(Result.error(e.getMessage()));
//        response.getWriter().write( result );
//        log.info("失败信息:"+e.getMessage());
//    }
//}
