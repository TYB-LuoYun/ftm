package top.anets;


import com.spring4all.swagger.EnableSwagger2Doc;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

@EnableFeignClients
@EnableDiscoveryClient // 开启 Nacos 服务注册发现
@EnableSwagger2Doc //启动Swagger
@SpringBootApplication(exclude= {DataSourceAutoConfiguration.class})
public class FtmFileApplication {
    public static void main(String[] args) { SpringApplication.run(FtmFileApplication.class); }
}



