package top.anets.oauth2.sentinel;

import com.alibaba.cloud.nacos.NacosConfigManager;
import com.alibaba.csp.sentinel.slots.block.degrade.DegradeRule;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.PropertyNamingStrategy;
import com.alibaba.fastjson.serializer.SerializeConfig;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.alibaba.nacos.api.config.ConfigType;
import com.alibaba.nacos.api.exception.NacosException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class DefaultRuleNacosManager {

    private static final Logger logger = LoggerFactory.getLogger(DefaultRuleNacosManager.class);

    @Autowired
    private NacosConfigManager nacosConfigManager;

    @Value("${spring.application.name}")
    private String appName;


    public String getConfig(String dataId, String group) {
        try {
            return nacosConfigManager.getConfigService().getConfig(dataId, group, 10000l);
        } catch (NacosException e) {
            logger.error("NacosService publish e:{}", e);
        }
        return null;
    }


    private Boolean publish(String dataId, String group, String content, String type) {
        try {
            return nacosConfigManager.getConfigService().publishConfig(dataId, group, content, type);
        } catch (NacosException e) {
            logger.error("NacosService publish e:{}", e);
        }
        return false;
    }

    public void pushRules(List<DegradeRule> localDegradeRuleList) {
        String dataId = appName + CommonConstant.DEGRADE_DATA_ID_POSTFIX;

        SerializeConfig serializeConfig = new SerializeConfig();
        serializeConfig.propertyNamingStrategy = PropertyNamingStrategy.CamelCase;
        String contentStr = JSON.toJSONString(localDegradeRuleList, serializeConfig, SerializerFeature.PrettyFormat);
        publish(dataId, CommonConstant.GROUP_ID, contentStr, ConfigType.JSON.getType());
    }


    public void proess(List<DegradeRule> localDegradeRuleList, List<DegradeRule> remoteDegradeRuleList) {
        for (DegradeRule rule : remoteDegradeRuleList) {
            if (localDegradeRuleList.contains(rule)) {
                DegradeRule ldr = localDegradeRuleList.get(localDegradeRuleList.indexOf(rule));
                if (ldr.equals(rule)) {
                    continue;
                }
                localDegradeRuleList.remove(ldr);
                localDegradeRuleList.add(rule);
            } else {
                localDegradeRuleList.add(rule);
            }
        }
    }


    public List<DegradeRule> fetchRemoteRules() {
        String dataId = appName + CommonConstant.DEGRADE_DATA_ID_POSTFIX;
        return JSONObject.parseArray(this.getConfig(dataId, CommonConstant.GROUP_ID), DegradeRule.class);
    }
}