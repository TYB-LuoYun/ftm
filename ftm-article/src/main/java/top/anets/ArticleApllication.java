package top.anets;

import com.spring4all.swagger.EnableSwagger2Doc;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.security.oauth2.client.EnableOAuth2Sso;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

@EnableFeignClients
@EnableDiscoveryClient // 开启 Nacos 服务注册发现
@EnableSwagger2Doc //启动Swagger
@SpringBootApplication
@MapperScan("top.anets.article.mapper")
public class SystemApllication {
    public static void main(String[] args) { SpringApplication.run(SystemApllication.class); }
}