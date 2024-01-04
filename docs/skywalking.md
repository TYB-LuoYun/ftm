### skywalking 安装

#### 引入依赖，版本要在8.4.0以上
<dependency>
            <groupId>org.apache.skywalking</groupId>
            <artifactId>apm-toolkit-logback-1.x</artifactId>
            <version>8.5.0</version>
</dependency>



#### 在logback-spring.xml中配置

<!--  skywalking采集日志  -->
    <appender name="msystem-log" class="org.apache.skywalking.apm.toolkit.log.logback.v1.x.log.GRPCLogClientAppender">
            <!-- 日志输出编码 -->
            <encoder>
                <!--格式化输出：%d表示日期，%thread表示线程名，%-5level：级别从左显示5个字符宽度%msg：日志消息，%n是换行符-->
                <pattern>%d{yyyy-MM-dd HH:mm:ss.SSS} [%thread] %-5level %logger{50} - %msg%n
                </pattern>
            </encoder>
     </appender>
        
    <root level="INFO"> 
            <appender-ref ref="msystem-log"/>
    </root>
