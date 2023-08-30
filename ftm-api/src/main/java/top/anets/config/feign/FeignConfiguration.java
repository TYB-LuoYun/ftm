package top.anets.config.feign;

import com.alibaba.csp.sentinel.SphU;
import feign.Feign;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Scope;

/**
 * @author ftm
 * @date 2023-08-25 17:03
 * 复杂分布式体系结构中的应用程序有数十个依赖关系，每个依赖关系在某些时候将不可避免失败！
 *
 *
 * 服务熔断，假设服务宕机或者在单位时间内调用服务失败的次数过多，即服务降级的次数太多，那么则服务熔断
 *         比如A->B->C链式调用(扇出) ：C服务不可用或者响应时间太长，B就会阻塞，耗尽线程资源，B可能也宕机不可用，A会阻塞 ....,避免服务雪崩的问题 ；
 *
 * 服务降级，服务降级的流程都是先调用正常的方法，再调用fallback的方法,主逻辑失败采用备用逻辑的过程;
 * 流控：   尽可能的把系统资源让给优先级高的服务,比如活动的时候，交易功能优先，其他评论功能可以降级
 *
 *
 * 雪崩三个流程:服务提供者不可用->重试会导致网络流量加大，更影响服务提供者->导致服务调用者不可用，由于服务调用者 一直等待返回，一直占用系统资源
 * 服务不可用原因:宕机，程序负载大响应超时(可能缓存击穿)，网络超时
 *
 *
 *
 * 1服务熔断的三种状态(状态机)：
 * 熔断打开：请求不再进行调用当前服务，直接走降级逻辑，内部设置时钟一般为MTTR（平均故障处理时间）当打开时长达到所设置时钟则进入半熔断状态
 * 熔断关闭：熔断关闭，不会对服务进行熔断，断路器放行所有请求，并开始统计异常比例、慢请求比例。超过阈值则切换到open状态
 * 熔断半开：半开状态，会放行一次请求尝试，如果请求成功且符合规则认为当前服务恢复正常，关闭熔断，恢复链路。
 *
 *

 *
 * Hystrix会监控微服务间调用的状况，当失败的调用达到一定阈值，缺省是5秒内20次调用失败，就会启动熔断机制
 * 当满足一定的阀值的时候 (默认10秒内超过20个请求次数)
 * 当失败率达到一定的时候 (默认10秒内超过50%的请求失败)
 * 到达以上阀值，断路器将会开启
 * 当开启的时候，所有请求都不会进行转发
 * 一段时间之后(默认是5秒) ，这个时候断路器是半开状态，会让其中一个请求进行转发如果成功，断路器会关闭，若失败，继续开启。
 */
@Configuration
public class FeignConfiguration {
    @Bean
    @Scope("prototype")
    @ConditionalOnClass({SphU.class,Feign.class})
    @ConditionalOnProperty(name ="feign.sentinel.enabled")
    @Primary
    public Feign.Builder feignSentinelBuilder() {
        return CustomSentinelFeign.builder();
    }



}
