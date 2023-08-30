# Sentinel-Dashboard-Nacos

原始博客地址：[https://www.cnblogs.com/cndarren/p/16197684.html](https://www.cnblogs.com/cndarren/p/16197684.html)

## 0.配置项

- 下载sentinel-dashboard-nacos-1.8.1.jar
- 修改application.properties

| 配置项          | 说明                            | 默认值         |
| --------------- | ------------------------------- | -------------- |
| auth.username   | sentinel登录的用户名            | sentinel       |
| auth.password   | sentinel登录的密码              | sentinel       |
| server.port     | sentinel工程的端口              | 8080           |
| nacos.address   | nacos的主机id                   | 127.0.0.1:8848 |
| nacos.namespace | nacos存放sentinel规则的命名空间 | public         |
| nacos.username  | nacos登录的用户名               | nacos          |
| nacos.password  | nacos登录的密码                 | nacos          |

## 1.部署

- 下载`sentinel-dashboard-nacos-1.8.1.jar`

`docker-compose.yml`

```yml
version: "3"
services:
  # 流量防卫兵
  sentinel-dashboard:
    build: ./
    image: sentinel-dashboard-nacos:1.8.1
    container_name: sentinel-dashboard-nacos
    restart: always
    ports: #避免出现端口映射错误，建议采用字符串格式 8080端口为Dockerfile中EXPOSE端口
      - "8858:8080"
    volumes:
      - "./sentinel/logs:/root/logs"
```

`Dockerfile`

```
# 运行环境 java 8
FROM java:8
# 切换到目录/usr/local
WORKDIR /usr/local
# 复制./sentinel-dashboard-nacos-1.8.1.jar 到容器
ADD ./sentinel-dashboard-nacos-1.8.1.jar .
# 运行jar文件
CMD ["java","-jar","sentinel-dashboard-nacos-1.8.1.jar"]
# 工程暴露的端口
EXPOSE 8080
```

## 2.集成到项目

- 导入依赖

```xml
        <dependency>
            <groupId>com.alibaba.cloud</groupId>
            <artifactId>spring-cloud-starter-alibaba-sentinel</artifactId>
        </dependency>
        <dependency>
            <groupId>com.alibaba.csp</groupId>
            <artifactId>sentinel-datasource-nacos</artifactId>
        </dependency>
```

