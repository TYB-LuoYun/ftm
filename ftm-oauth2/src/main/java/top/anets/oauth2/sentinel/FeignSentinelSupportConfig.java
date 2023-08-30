package top.anets.oauth2.sentinel;

/**
 * @author ftm
 * @date 2023-08-28 17:28
 */
import com.alibaba.cloud.nacos.NacosConfigManager;
import com.alibaba.csp.sentinel.slots.block.degrade.circuitbreaker.CircuitBreakerStrategy;
import com.alibaba.csp.sentinel.slots.block.degrade.DegradeRule;
import com.alibaba.csp.sentinel.slots.block.degrade.DegradeRuleManager;
import com.alibaba.csp.sentinel.util.StringUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.PropertyNamingStrategy;
import com.alibaba.fastjson.serializer.SerializeConfig;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.alibaba.nacos.api.config.ConfigType;
import com.alibaba.nacos.api.exception.NacosException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.context.EnvironmentAware;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class FeignSentinelSupportConfig implements ApplicationRunner, EnvironmentAware {

    private static final Logger logger = LoggerFactory.getLogger(FeignSentinelSupportConfig.class);

    private final static String HTTP_PROTOCOL_PREFIX = "http://";
    private final static String ANNOTATION_VALUE_PREFIX = "${";
    private final static String ANNOTATION_VALUE_SUFFIX = "}";
    private Environment environment;//环境变量对象

    @Value("${spring.cloud.nacos.config.enabled:}")
    private Boolean nacosConfigEnable;

    @Value("${spring.application.name:}")
    private String appName;
    @Value("${spring.cloud.sentinel.transport.clientIp}")
    private String IP;
    @Value("${spring.cloud.sentinel.transport.port}")
    private Integer PORT;

    private Class<?> deduceMainApplicationClass() {
        try {
            StackTraceElement[] stackTrace = new RuntimeException().getStackTrace();
            for (StackTraceElement stackTraceElement : stackTrace) {
                if ("main".equals(stackTraceElement.getMethodName())) {
                    return Class.forName(stackTraceElement.getClassName());
                }
            }
        } catch (ClassNotFoundException ex) {

        }
        return null;
    }

    @Override
    public void run(ApplicationArguments args) {
        Class<?> mainClass = deduceMainApplicationClass();
        logger.info("开始加载默认规则，mainClass:{}", mainClass);
        if (mainClass == null) {
            throw new RuntimeException("can not fount main class");
        }
        // 添加开关 如果不使用nacos配置中心，则直接return，不初始化默认的熔断规则
        if (nacosConfigEnable != null && !nacosConfigEnable) {
            logger.info("nacos配置中心关闭，加载默认规则结束");
            return;
        }
        EnableFeignClients enableFeignClientsAnnotation = mainClass.getAnnotation(EnableFeignClients.class);
        if (enableFeignClientsAnnotation != null) {
            String[] feignClientPackages;
            String[] feignClientDeclaredPackages = enableFeignClientsAnnotation.basePackages();
            //声明了feignClient的包名
            if (feignClientDeclaredPackages.length == 0) {
                feignClientPackages = new String[]{mainClass.getPackage().getName()};
            } else {
                feignClientPackages = feignClientDeclaredPackages;
            }
            //初始化降级规则
            initDeGradeRule(feignClientPackages);
        }
        logger.info("默认降级规则处理完成");
    }

    private Set<Class> getFeignClientClass(String[] packageNames) {
        ClassScanner classScanner = new ClassScanner();
        Set<Class> feignClientClass = classScanner.scan(packageNames, FeignClient.class);
        return feignClientClass;
    }

    /**
     * 创建熔断规则列表数据
     * 如果全部feign接口设置默认策略太多，则可以通过自定义注解，标记哪些feign接口类需要设置
     *
     * @param cla feign接口类
     * @return
     */
    public List<NacosDegradeRule> initRules(Class cla) {
        List<NacosDegradeRule> degradeRuleList = new ArrayList<>();
        // 在该处获取自定义注解，来判断是否添加默认熔断规则  如果feign接口类上添加了@EnableFeignDegrade注解并且enable为false，则不为该类中接口添加默认熔断规则
        EnableFeignDegrade feignDegrade = (EnableFeignDegrade) cla.getAnnotation(EnableFeignDegrade.class);
        if (null != feignDegrade && !feignDegrade.enable()) {
            logger.info("{}类关闭了初始化默认熔断规则功能", cla.getName());
            return degradeRuleList;
        }

        FeignClient feignClient = (FeignClient) cla.getAnnotation(FeignClient.class);
        // 获取RequestMapping注解是否配置基础路径
        String classRequestMappingUrl = "";
        RequestMapping classRequestMapping = (RequestMapping) cla.getAnnotation(RequestMapping.class);
        if (null != classRequestMapping) {
            classRequestMappingUrl = classRequestMapping.value()[0];
        }
        // 这里区分四种情况
        // 1.@FeignClient(name = "demo01", url = "http://127.0.0.1:36001"
        // 2.@FeignClient(name = "demo01", url = "${base.url}"
        // 3.@FeignClient(name = "demo01"
        // 4.@FeignClient(name = "${demo01.serviceName}"
        // 标识是否配置url属性，拼接http://前缀时判断使用
        Boolean httpFlag = true;
        String baseUrl = null;
        String serviceUrl = feignClient.url();
        String serviceName = feignClient.name();
        // 如果url属性不为空并且${开头 }结尾，说明是动态配置的url
        if (StringUtil.isNotBlank(serviceUrl) && serviceUrl.startsWith(ANNOTATION_VALUE_PREFIX) && serviceUrl.endsWith(ANNOTATION_VALUE_SUFFIX)) {
            baseUrl = environment.resolvePlaceholders(serviceUrl);
        } else if (StringUtil.isNotBlank(serviceUrl)) {
            // 如果http路径最后一位是/ 则去掉斜杠
            baseUrl = !serviceUrl.endsWith("/") ? serviceUrl : serviceUrl.substring(0, serviceUrl.length() - 1);
        } else if (StringUtil.isBlank(serviceUrl) && serviceName.startsWith(ANNOTATION_VALUE_PREFIX) && serviceName.endsWith(ANNOTATION_VALUE_SUFFIX)) {
            baseUrl = environment.resolvePlaceholders(serviceName);
            httpFlag = false;
        } else {
            baseUrl = serviceName;
            httpFlag = false;
        }

        Method[] methods = cla.getDeclaredMethods();
        for (Method method : methods) {
            degradeRuleList.add(buildDegradeRule(getResourceName(classRequestMappingUrl, baseUrl, method, httpFlag)));
        }
        List<DegradeRule> collect = degradeRuleList.stream().map(e -> {
            DegradeRule degradeRule = new DegradeRule();
            BeanUtils.copyProperties(e, degradeRule);
            return degradeRule;
        }).collect(Collectors.toList());
        DegradeRuleManager.loadRules(collect);
        return degradeRuleList;

    }

    /**
     * 初始化熔断规则
     *
     * @param feignClientPackages
     */
    private void initDeGradeRule(String[] feignClientPackages) {
        List<NacosDegradeRule> localDegradeRuleList = new ArrayList<>();
        Set<Class> feignClientClass = getFeignClientClass(feignClientPackages);
        for (Class clientClass : feignClientClass) {
            List<NacosDegradeRule> rules = initRules(clientClass);
            localDegradeRuleList.addAll(rules);
        }

        NacosConfigManager nacosConfigManager = SpringBeanUtil.getBean("nacosConfigManager", NacosConfigManager.class);
        List<NacosDegradeRule> remoteDegradeRuleList = this.fetchRemoteRules(nacosConfigManager);
        //远程nacos没有规则，那就直接利用本地规则
        if (remoteDegradeRuleList == null || remoteDegradeRuleList.isEmpty()) {
            this.pushRules(nacosConfigManager, localDegradeRuleList);
            return;
        }
        //本地规则 合并 远程规则策略
        this.proess(localDegradeRuleList, remoteDegradeRuleList);
        //推送本地规则，到nacos ---->改成推送远程的规则，以远程的为准，没有的加进去
        this.pushRules(nacosConfigManager, remoteDegradeRuleList);
    }

    /***
     * 组装resourceName
     * @param crmu requestMapping路径
     * @param serviceName 服务名称或url
     * @param method 方法
     * @param httpFlag http标记
     * @return
     */
    private String getResourceName(String crmu, String serviceName, Method method, Boolean httpFlag) {
//        crmu = crmu.startsWith("/") ? crmu : "/" + crmu;
        String resourceName = "";
        RequestMapping methodRequestMapping = method.getAnnotation(RequestMapping.class);
        if (null != methodRequestMapping) {
            String mrm = methodRequestMapping.value()[0];
            // 获取@RequestMapping中的method参数，如果未配置，则默认为GET请求
            RequestMethod mMethod = null;
            if (methodRequestMapping.method().length > 0) {
                mMethod = methodRequestMapping.method()[0];
            }
            // @RequestMapping 注解不指定method参数，则默认为GET请求
            if (!httpFlag) {
                if (mMethod != null) {
                    resourceName = mMethod + ":" + HTTP_PROTOCOL_PREFIX + serviceName + crmu + (mrm.startsWith("/") ? mrm : "/" + mrm);
                } else {
                    resourceName = "GET:" + HTTP_PROTOCOL_PREFIX + serviceName + crmu + (mrm.startsWith("/") ? mrm : "/" + mrm);
                }
            } else {
                if (mMethod != null) {
                    resourceName = mMethod + ":" + serviceName + crmu + (mrm.startsWith("/") ? mrm : "/" + mrm);
                } else {
                    resourceName = "GET:" + serviceName + crmu + (mrm.startsWith("/") ? mrm : "/" + mrm);
                }
            }
        }
        PostMapping methodPostMapping = method.getAnnotation(PostMapping.class);
        if (null != methodPostMapping) {
            String mpm = methodPostMapping.value()[0];
            // 未配置url属性，说明使用服务名进行服务之间调用，所以需要拼接http://  拼接结果：http://serviceName/demo/get  前面拼接的请求方式(POST:)是因为sentinel需要 不拼接熔断规则不生效
            if (!httpFlag) {
                resourceName = "POST:" + HTTP_PROTOCOL_PREFIX + serviceName + crmu + (mpm.startsWith("/") ? mpm : "/" + mpm);
            } else { // 配置url属性，说明使用指定url调用 则不需要单独拼接http://  拼接结果为：http://127.0.0.1:8080/demo/get  前面拼接的请求方式(POST:)是因为sentinel需要 不拼接熔断规则不生效
                resourceName = "POST:" + serviceName + crmu + (mpm.startsWith("/") ? mpm : "/" + mpm);
            }
        }
        GetMapping methodGetMapping = method.getAnnotation(GetMapping.class);
        if (null != methodGetMapping) {
            String mgm = methodGetMapping.value()[0];
            if (!httpFlag) {
                resourceName = "GET:" + HTTP_PROTOCOL_PREFIX + serviceName + crmu + (mgm.startsWith("/") ? mgm : "/" + mgm);
            } else {
                resourceName = "GET:" + serviceName + crmu + (mgm.startsWith("/") ? mgm : "/" + mgm);
            }
        }
        return resourceName;
    }

    /***
     * 创建默认熔断规则
     * @param resourceName
     * @return
     */
    private NacosDegradeRule buildDegradeRule(String resourceName) {

//        DegradeRule rule = new DegradeRule(resourceName)
//                // 熔断策略
//                .setGrade(CircuitBreakerStrategy.SLOW_REQUEST_RATIO.getType())
//                // Max allowed response time  RT 慢调用标准值 接口响应时长超过2秒则被判定为慢调用
//                .setCount(2000)
//                // Retry timeout (in second)   窗口期  熔断时长
//                .setTimeWindow(30)
//                // Circuit breaker opens when slow request ratio > 80%  慢调用比例   判定是否熔断的条件之一
//                .setSlowRatioThreshold(0.8)
//                // 单位时长最小请求数   判定是否熔断的条件之一
//                .setMinRequestAmount(30)
//                // 统计时长也叫单位时长
//                .setStatIntervalMs(60000);
        NacosDegradeRule rule = new NacosDegradeRule();
        rule.setApp(appName);
        rule.setIp(IP);
        rule.setPort(PORT);
        rule.setLimitApp("default");

        rule.setResource(resourceName);
                // 熔断策略
        rule.setGrade(CircuitBreakerStrategy.SLOW_REQUEST_RATIO.getType());
                // Max allowed response time  RT 慢调用标准值 接口响应时长超过2秒则被判定为慢调用
        rule.setCount(2000);
                // Retry timeout (in second)   窗口期  熔断时长
        rule.setTimeWindow(30);
                // Circuit breaker opens when slow request ratio > 80%  慢调用比例   判定是否熔断的条件之一
        rule.setSlowRatioThreshold(0.8);
                // 单位时长最小请求数   判定是否熔断的条件之一
        rule.setMinRequestAmount(30);
                // 统计时长也叫单位时长
        rule.setStatIntervalMs(60000);
        return rule;
    }


    @Override
    public void setEnvironment(Environment environment) {
        this.environment = environment;
    }

    /************************************ nacos config ********************************************************/

    public String getConfig(NacosConfigManager nacosConfigManager, String dataId, String group) {
        try {
            return nacosConfigManager.getConfigService().getConfig(dataId, group, 10000l);
        } catch (NacosException e) {
            logger.error("NacosService publish e:{}", e);
        }
        return null;
    }


    private Boolean publish(NacosConfigManager nacosConfigManager, String dataId, String group, String content, String type) {
        try {
            return nacosConfigManager.getConfigService().publishConfig(dataId, group, content, type);
        } catch (NacosException e) {
            logger.error("NacosService publish e:{}", e);
        }
        return false;
    }

    public void pushRules(NacosConfigManager nacosConfigManager, List<NacosDegradeRule> localDegradeRuleList) {

//        String appName = (String) getAttrBootstrapYml("spring.application.name");
//        if (StringUtil.isBlank(appName)) {
//            appName = (String) getAttrBootstrapEnvYml("spring.application.name");
//        }
        String dataId = appName + CommonConstant.DEGRADE_DATA_ID_POSTFIX;
        SerializeConfig serializeConfig = new SerializeConfig();
        serializeConfig.propertyNamingStrategy = PropertyNamingStrategy.CamelCase;
        String contentStr = JSON.toJSONString(localDegradeRuleList, serializeConfig, SerializerFeature.PrettyFormat);
        publish(nacosConfigManager, dataId, CommonConstant.GROUP_ID, contentStr, ConfigType.JSON.getType());
    }


    public void proess(List<NacosDegradeRule> localDegradeRuleList, List<NacosDegradeRule> remoteDegradeRuleList) {
        ArrayList<NacosDegradeRule> newLocal = new ArrayList<>();
        for(NacosDegradeRule  local : localDegradeRuleList){
            boolean isHave = false;
            for (NacosDegradeRule remote : remoteDegradeRuleList) {
                if(local.getResource().equals(remote.getResource())){
                    isHave = true;
                }
            }
            //远程没有这个资源的配置
            if(isHave == false){
                newLocal.add(local);
            }
        }
//      遍历完成后
        remoteDegradeRuleList.addAll(newLocal);
    }


    public List<NacosDegradeRule> fetchRemoteRules(NacosConfigManager nacosConfigManager) {
//        String appName = (String) getAttrBootstrapYml("spring.application.name");
//        if (StringUtil.isBlank(appName)) {
//            appName = (String) getAttrBootstrapEnvYml("spring.application.name");
//        }
        String dataId = appName + CommonConstant.DEGRADE_DATA_ID_POSTFIX;
        return JSONObject.parseArray(this.getConfig(nacosConfigManager, dataId, CommonConstant.GROUP_ID), NacosDegradeRule.class);
    }
}