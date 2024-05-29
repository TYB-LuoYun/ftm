package top.anets.boot.module.webservice.controller;

import lombok.extern.slf4j.Slf4j;
import org.apache.cxf.jaxws.endpoint.dynamic.JaxWsDynamicClientFactory;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import top.anets.boot.exception.ServiceException;

import java.util.Map;

/**
 * @author ftm
 * @date 2024-01-09 10:38
 */
@Slf4j
@RequestMapping("webservice")
@RestController
public class TestController {
    @RequestMapping("ping")
    public String get(@RequestParam(defaultValue = "http://127.0.0.1:2398/test/user?wsdl") String url,@RequestParam(defaultValue = "getTeacherName")String method,@RequestParam(defaultValue = "411001")String params){
        log.debug("请求开始");
        JaxWsDynamicClientFactory dcf = JaxWsDynamicClientFactory.newInstance();
        org.apache.cxf.endpoint.Client client =dcf.createClient(url);

        // getUser 为接口中定义的方法名称  张三为传递的参数   返回一个 Object 数组
        Object[] objects= new Object[0];
        try {
            objects = client.invoke(method,new Object[]{params});
        } catch (Exception e) {
            e.printStackTrace();
            throw new ServiceException(e.getMessage());
        }
        // 输出调用结果
        log.debug("*****"+objects[0].toString());
        log.debug("请求结束");
        return objects[0].toString();
    }


    @RequestMapping("post")
    public String post(@RequestBody Map<String,Object> map){
        log.debug("请求开始");
        JaxWsDynamicClientFactory dcf = JaxWsDynamicClientFactory.newInstance();
        org.apache.cxf.endpoint.Client client =dcf.createClient((String) map.get("url"));

        // getUser 为接口中定义的方法名称  张三为传递的参数  返回一个 Object 数组
        Object[] objects= new Object[0];
        try {
            objects = client.invoke((String) map.get("method"),new Object[]{map.get("params")});
        } catch (Exception e) {
            e.printStackTrace();
            throw new ServiceException(e.getMessage());
        }
        // 输出调用结果
        log.debug("*****"+objects[0].toString());
        log.debug("请求结束");
        return objects[0].toString();
    }

}
