package top.anets.search;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@EnableDiscoveryClient // 开启 Nacos 服务注册发现
@SpringBootApplication
public class SearchApplication {
        public static void main(String[] args) { SpringApplication.run(SearchApplication.class); }
}
