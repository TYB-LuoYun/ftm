package top.anets.cloud.base.feign.starter;

import com.alibaba.csp.sentinel.SphU;
import feign.Feign;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.*;
import top.anets.cloud.base.feign.config.CustomSentinelFeign;

/**
 * @author ftm
 * @date 2023/3/30 0030 10:09
 */
@Configuration
@Import({CloudBaseAutoConfiguration.class})
//@EnableConfigurationProperties(SPayProperties.class)
public class CloudBaseStarter {



}
