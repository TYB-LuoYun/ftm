package top.anets.boot.module.webservice.publish;

import org.apache.cxf.Bus;
import org.apache.cxf.bus.spring.SpringBus;
import org.apache.cxf.jaxws.EndpointImpl;
import org.apache.cxf.jaxws.endpoint.dynamic.JaxWsDynamicClientFactory;
import org.apache.cxf.transport.servlet.CXFServlet;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import top.anets.boot.module.webservice.service.TeacherService;
import top.anets.boot.module.webservice.service.TeacherServiceImpl;

import javax.xml.ws.Endpoint;

/**
 * @author ftm
 * @date 2024-01-09 9:58
 */
@Configuration
public class PublishWebService {

    // 如果这个地方的名字是 dispatcherServlet 则其他的请求也会被 webservice 拦截，访问不了
    @Bean
    public ServletRegistrationBean disServlet() {
        return new ServletRegistrationBean(new CXFServlet(), "/test/*");
    }

    @Bean(name = Bus.DEFAULT_BUS_ID)
    public SpringBus springBus() {
        return new SpringBus();
    }

    @Bean
    public TeacherService teacherService() {
        return new TeacherServiceImpl();
    }
    @Bean
    public Endpoint endpoint() {
        EndpointImpl endpoint = new EndpointImpl(springBus(), teacherService());
        endpoint.publish("/user");
        return endpoint;
    }


    public static void main(String[] args) throws Exception {

        JaxWsDynamicClientFactory dcf = JaxWsDynamicClientFactory.newInstance();
        org.apache.cxf.endpoint.Client client =dcf.createClient("http://192.168.168.12:2398/test/user?wsdl");

        // getUser 为接口中定义的方法名称  张三为传递的参数   返回一个 Object 数组
        Object[] objects=client.invoke("getTeacherName","411001");

        // 输出调用结果
        System.out.println("*****"+objects[0].toString());
    }

}
