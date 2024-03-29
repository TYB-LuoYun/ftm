package top.anets;

//import com.spring4all.swagger.EnableSwagger2Doc;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.cloud.client.circuitbreaker.EnableCircuitBreaker;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

@EnableFeignClients
@EnableDiscoveryClient // 开启 Nacos 服务注册发现
//@EnableSwagger2Doc //启动Swagger
@SpringBootApplication
//@EnableCircuitBreaker //hystrix的断路器，加上之后才会触发fallback机制
public class AuthServerApplication {
    public static void main(String[] args) { SpringApplication.run(AuthServerApplication.class); }
}


