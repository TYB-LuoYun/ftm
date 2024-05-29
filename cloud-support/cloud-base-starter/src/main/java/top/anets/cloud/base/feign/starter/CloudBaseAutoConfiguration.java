package top.anets.cloud.base.feign.starter;
import com.alibaba.cloud.sentinel.feign.SentinelFeignAutoConfiguration;
import com.alibaba.csp.sentinel.SphU;
import feign.Feign;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.AutoConfigureOrder;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.*;
import org.springframework.core.Ordered;
import top.anets.cloud.base.feign.config.CustomSentinelFeign;

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
@ComponentScan(basePackages = "top.anets.cloud.base.feign")
@AutoConfigureOrder(Ordered.HIGHEST_PRECEDENCE)
@AutoConfigureBefore(SentinelFeignAutoConfiguration.class)
public class CloudBaseAutoConfiguration {


//    @Bean
//    @ConditionalOnBean(WxPayConfig.class)
////    当你的bean被注册之后,如果而注册相同类型的bean,就不会成功,它会保证你的bean只有一个









}