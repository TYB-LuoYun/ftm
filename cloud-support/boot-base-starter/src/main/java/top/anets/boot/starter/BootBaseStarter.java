package top.anets.boot.starter;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/**
 * @author ftm
 * @date 2023/3/30 0030 10:09
 */
@Configuration
@Import({BootBaseAutoConfiguration.class})
//@EnableConfigurationProperties(SPayProperties.class)
public class BootBaseStarter {



}