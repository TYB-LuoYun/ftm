package top.anets.im;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * @author ftm
 * @date 2023-11-01 15:07
 */
@EnableDiscoveryClient // nacos客户端
@EnableFeignClients
@SpringBootApplication
public class ImApplication {
    public static void main(String[] args) {
        SpringApplication.run(ImApplication.class, args);
    }
}
