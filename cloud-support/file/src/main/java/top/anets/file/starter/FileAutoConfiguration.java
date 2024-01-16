package top.anets.file.starter;

import org.springframework.boot.autoconfigure.AutoConfigureOrder;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;

/**
 * @author ftm
 * @date 2023/3/30 0030 9:51
 */
@Configuration
//// 当存在某个类时，此自动配置类才会生效
//@ConditionalOnClass({WxPayStrategy.class,   PayContext.class})
//@EnableConfigurationProperties(SPayProperties.class)

//导入外部@Bean配置的类
//@Import(WxPayConfig.class)
@ComponentScan(basePackages = "top.anets.file")
@AutoConfigureOrder(Ordered.HIGHEST_PRECEDENCE)
public class FileAutoConfiguration {


//    @Bean
//    @ConditionalOnBean(WxPayConfig.class)
////    当你的bean被注册之后,如果而注册相同类型的bean,就不会成功,它会保证你的bean只有一个









}