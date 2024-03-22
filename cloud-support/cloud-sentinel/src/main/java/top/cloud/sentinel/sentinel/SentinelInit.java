package top.cloud.sentinel.sentinel;

import com.alibaba.csp.sentinel.slots.block.RuleConstant;
import com.alibaba.csp.sentinel.slots.block.degrade.DegradeRule;
import com.alibaba.csp.sentinel.slots.block.degrade.DegradeRuleManager;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * @author ftm
 * @date 2023-08-28 14:47
 */
@Component
@Order(value = 1)
public class SentinelInit implements ApplicationRunner {
    @Override
    public void run(ApplicationArguments args) throws Exception {
        initDegradeRule();
    }


    private static void initDegradeRule() {
//        List<DegradeRule> rules = new ArrayList<>();
//        DegradeRule rule = new DegradeRule();
//        rule.setResource("GET:http://system-server/feign/sysUser/findByUsername");
////        rule.setResource("findByUsername");
//        // set threshold RT, 10 ms
//        rule.setCount(1000);//设置平均响应时间为1000ms
//        /**
//         * 降级策略为:
//         *    DEGRADE_GRADE_RT - 平均响应时间（资源的平均响应时间超过阈值（DegradeRule 中的 count，以 ms 为单位）之后，资源进入准降级状态）
//         *    异常比例 (DEGRADE_GRADE_EXCEPTION_RATIO)：当资源的每秒异常总数占通过量的比值超过阈值（DegradeRule 中的 count）之后，资源进入降级状态，即在接下的时间窗口（DegradeRule 中的 timeWindow，以 s 为单位）之内，对这个方法的调用都会自动地返回。异常比率的阈值范围是 [0.0, 1.0]，代表 0% - 100%。
//         *    异常数 (DEGRADE_GRADE_EXCEPTION_COUNT)：当资源近 1 分钟的异常数目超过阈值之后会进行熔断
//         */
//        rule.setGrade(RuleConstant.DEGRADE_GRADE_RT);//降级模式，根据平均响应时间降级
//        rule.setTimeWindow(20);//设置降级持续时间为20s
//        rule.setMinRequestAmount(1);
////        说明：当持续进入的5个请求响应时间都超过1000ms，接下来的20s内对这个接口的调用都会直接返回并抛出DegradeException，然后执行降级方法
//        rules.add(rule);
//        DegradeRuleManager.loadRules(rules);
    }
}
