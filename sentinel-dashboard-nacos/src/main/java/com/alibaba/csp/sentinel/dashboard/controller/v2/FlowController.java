package com.alibaba.csp.sentinel.dashboard.controller.v2;

import com.alibaba.csp.sentinel.dashboard.auth.AuthAction;
import com.alibaba.csp.sentinel.dashboard.auth.AuthService;
import com.alibaba.csp.sentinel.dashboard.datasource.entity.rule.FlowRuleEntity;
import com.alibaba.csp.sentinel.dashboard.domain.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author ftm
 * @date 2023-10-09 17:47
 */
@RestController
public class FlowController {
    @Autowired
    private FlowControllerV2 flowControllerV2;

    @PostMapping("/flow/rule")
    @AuthAction(value = AuthService.PrivilegeType.WRITE_RULE)
    public Result<FlowRuleEntity> flueRule(@RequestBody FlowRuleEntity entity){
       return flowControllerV2.apiAddFlowRule(entity);
    }
}
