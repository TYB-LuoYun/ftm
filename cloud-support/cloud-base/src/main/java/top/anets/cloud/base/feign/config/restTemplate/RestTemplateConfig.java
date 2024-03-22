package top.anets.cloud.base.feign.config.restTemplate;

/**
 * @author ftm
 * @date 2024-01-23 14:45
 * feign是RestTemplate的高级组件实现。feign中RestTemplate默认集成了 @LoadBalanced
 * 用RestTemplate注解可以打开
 * Feign实现负载均衡已经实现，默认情况下Feign使用轮询算法
 * 修改算法用:
  ribbon:
    NFLoadBalancerRuleClassName: com.netflix.loadbalancer.RandomRule
 */
//@Configuration
//public class RestTemplateConfig {
//
//    @Bean
//    @LoadBalanced
//    public RestTemplate restTemplate() {
//        SimpleClientHttpRequestFactory requestFactory = new          SimpleClientHttpRequestFactory();
//        requestFactory.setConnectTimeout(1000);
//        requestFactory.setReadTimeout(1000);
//
//        RestTemplate restTemplate = new RestTemplate(requestFactory);
//        return restTemplate;
//        return new RestTemplate();
//    }
//}
